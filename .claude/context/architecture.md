# Mosaic — Architecture Reference

## Module Dependency Graph

```
mosaic-core
    ↑ (depends on)
    ├── mosaic-client   (deserializes + renders)
    ├── mosaic-server   (serializes + DSL)
    ├── sample-client   (consumes client + core)
    └── sample-server   (consumes server + core)
```

`mosaic-core` has no dependencies on other Mosaic modules. It is the single source of truth for all contracts (Schemas).

---

## Core Pattern: Schema → Builder → Renderer

Every component in Mosaic follows this pattern across three layers:

| Layer | Module | Type | Responsibility |
|---|---|---|---|
| Schema | `mosaic-core` | `data class` | Typed data contract. `@Serializable`, `@SerialName`. Registered in `MosaicSerializer`. |
| Builder | `mosaic-server` | `class` / `object` | DSL class that constructs a Schema type-safely. Exposed as extension function on a scope. |
| Definition | `mosaic-client` | `object` | Binds a Schema class to its Renderer and HolderBuilder. |
| Renderer | `mosaic-client` | `object` | `@Composable` that receives a Schema and renders it via Compose. |
| Holder | `mosaic-client` | `class` | Stateful wrapper around a Schema. Manages updates and child hierarchy. |
| HolderBuilder | `mosaic-client` | `object` | Factory that produces a Holder from a Schema. |
| Runner | `mosaic-client` | `object` | Executes the logic of an EventSchema within an `EventRunningScope`. |

---

## System Map: Who Talks to Whom

```
MosaicScreen (Composable)
  │
  ├── MosaicScreenStateHolder (ViewModel)
  │     │
  │     ├── TilesManager (implements 5 interfaces)
  │     │     ├── TileHolderBuilderManager → TileHolderBuilder implementations
  │     │     │     └── TileHolder tree (mutable, hierarchical)
  │     │     │           └── EventHolder children
  │     │     └── EventHolderBuilderManager → EventHolderBuilder implementations
  │     │
  │     ├── EventManager
  │     │     ├── EventRunnerManager → EventRunner implementations
  │     │     └── creates EventRunningScope (per event execution)
  │     │           ├── tilesEditor          (→ TilesManager)
  │     │           ├── tilesOverlaysEditor  (→ TilesManager)
  │     │           ├── tilesEventDispatcher (→ TilesManager)
  │     │           ├── dataHolder           (→ MosaicScreenStateHolder)
  │     │           ├── screenBehaviorsHolder(→ MosaicScreenStateHolder)
  │     │           └── Koin DI access
  │     │
  │     ├── TileRendererManager → TileRenderer implementations
  │     │     └── creates TileRenderingScope (per tile render)
  │     │           ├── dispatchEvent / triggerEvent (→ UIEvent → StateHolder)
  │     │           └── RenderChild / RenderChildren
  │     │
  │     └── DefaultDataHolder (in-memory, screen-scoped)
  │
  └── CompositionLocals
        ├── LocalTileRendererManager
        └── LocalBroadcastChannel (SharedFlow<BroadcastData>)

Koin DI (global)
  ├── NavigatorsHolder
  ├── DataMailer
  ├── MosaicDatabase (SQLDelight)
  ├── MosaicFileSystem
  ├── MosaicRepository
  └── DataProcessor implementations
```

---

## Full Data Flow: JSON → UI

```
1. JSON String (HTTP response or local)
        │
        ▼  MosaicSerializer.decodeFromJsonElement()
2. TileSchema / EventSchema  (typed, immutable data classes)
        │
        ▼  TileHolderBuilderManager + EventHolderBuilderManager (via BuilderScope)
3. TileHolder<T> / EventHolder<T>  (mutable state, full child hierarchy)
        │
        ▼  TilesManager.updateState()
4. MosaicScreenStateHolder.uiState: StateFlow<State>
        │
        ▼  Compose collectAsState() → recomposition
5. TileRendererManager.Render(tileSchema, onEvent)
        │   checks isGone(), looks up tileSchema::class → TileRenderer<T>
        ▼  TileRenderingScope.Render(tileSchema)
6. Composable UI (Material 3 components)
```

---

## Full Event Flow: User Interaction → Action

```
1. User interaction in Compose (e.g., button click)
        │
        ▼  TileRenderingScope.triggerEvent(EventTriggers.onClick())
2. UIEvent.EventSchemaHolderUIEvent emitted via onEvent callback
        │
        ▼  MosaicScreenStateHolder.onEvent(Event.OnUIEvent)
3. onUIEvent() routes:
        ├── TileEventHolderUIEvent   → TilesManager.onEvent(tileId, event)
        ├── TileGroupEventHolderUIEvent → TilesManager.onGroupEvent(event)
        └── EventSchemaHolderUIEvent → EventManager.runEvents(events, data)
                │
                ▼  EventRunnerManager looks up EventSchema::class → EventRunner<T>
4. EventRunningScope created with all dependencies
        │
        ▼  EventRunner<T>.runEvent(EventRunningScope)
5. Action executed (navigate, update tiles, network request, persist data, etc.)
        │
        ▼  mutations call TilesManager.updateState()
6. StateFlow updated → Compose recomposes
```

---

## Full Suspend Flow: Async Operations in EventRunners

```
EventRunner.runEvent() {
    runSuspendOnScreenScope {              // launches on screenCoroutineScope
        withContext(Dispatchers.IO) {     // IO-bound work
            // use cases, network, DB
        }
        // UI mutations back on main
        tilesEditor.updateTile(...)
        onTrigger(EventTriggers.onSuccess())
    }
}

screenCoroutineScope  →  tied to Compose's rememberCoroutineScope (UI lifetime)
stateHolderCoroutineScope  →  tied to ViewModel's viewModelScope (screen lifetime)
```

Both scopes use `SupervisorJob`: a failure in one launched block does not cancel the entire scope.

---

## UIEvent Routing (Sealed Interface)

```kotlin
sealed interface UIEvent {
    // Fired by TileRenderingScope.triggerEvent() — routes to EventManager
    data class EventSchemaHolderUIEvent(
        val events: List<EventSchema>,
        val data: Any?
    ) : UIEvent

    // Fired by TileRenderingScope.dispatchEvent() — routes to TilesManager
    data class TileEventHolderUIEvent(
        val tileId: String,
        val event: TileEvent          // marker interface
    ) : UIEvent

    // Fired by TileRenderingScope.dispatchGroupEvent() — routes to TilesManager
    data class TileGroupEventHolderUIEvent(
        val event: TileGroupEvent     // marker interface
    ) : UIEvent
}
```

---

## MosaicScreenStateHolder: The Orchestrator

Extends `ScreenStateHolder<State, Event, Effect>` (ViewModel base).
Implements `ScreenBehaviorsHolder`.
Delegates `DataHolder` to `DefaultDataHolder`.

**State machine:**
```
Initial ──onSuccess──► Displaying
Initial ──onFailure──► Failure
```

**Owns:**
- `internalUIState: MutableStateFlow<State>` — drives UI recomposition
- `internalBroadcastChannel: MutableSharedFlow<BroadcastData>` — cross-tile communication
- `TilesManager` — tile state
- `EventManager` — event execution
- `TileRendererManager` — rendering registry (via Koin)
- `DefaultDataHolder` — in-memory screen data

**Lifecycle hooks (ScreenStateHolder):**
- `onFirstDisplay()` — called once on first appearance
- `onDisplay()` — called every time the screen appears
- `onPause()` — screen goes background
- `onStop()` — ViewModel cleared, scope cancelled

---

## TilesManager: The Tile State Coordinator

Implements five behavior interfaces simultaneously:

| Interface | Responsibility |
|---|---|
| `TilesEditor` | add / remove / replace / update / wipe tiles |
| `TilesStateUpdater` | `updateState()` → triggers recomposition |
| `TilesEventDispatcher` | `onEvent(tileId, TileEvent)` / `onGroupEvent(TileGroupEvent)` |
| `TilesOverlaysEditor` | `setBottomSheetTiles()` / `setDialogTiles()` |
| `TilesEventHolder` | `getEventsByTrigger(trigger)` |

All mutations operate on the root `ScreenTileHolder` tree. After every mutation, `updateState()` is called, which invokes `onUpdateRequest(rootTile.get())` — updating the `StateFlow` and triggering Compose recomposition.

**ScreenTileHolder** is the root node. It extends `TileHolder` and additionally manages:
- `currentBottomSheetTiles: List<TileHolder<*>>?`
- `currentDialogSheetTiles: List<TileHolder<*>>?`
- `navigationDrawerTiles: List<TileHolder<*>>?`

Search traversal priority: bottomSheet → dialog → navigationDrawer → main tiles → events.

---

## TileHolder: The Stateful Tree Node

Abstract class (always `class`, never `object`). Manages mutable schema + child hierarchy.

**Key capabilities:**
- `var tile: T` — mutable schema reference (updated in-place by `update()`)
- `tiles: MutableList<TileHolder<*>>?` — child tiles
- `events: MutableList<EventHolder<*>>?` — associated events
- `get(): T` — returns schema with all children's current state
- `update(updateData: Map<String, Any?>)` — JSON merge update (no reflection)
- `getTileHolder(tileId)` — recursive depth-first search
- `getEventsByTrigger(trigger)` — finds matching events recursively
- `onTileEvent(event: TileEvent)` — overridable tile-specific event handler
- `onTileGroupEvent(event: TileGroupEvent)` — overridable group event handler

**JSON-based update mechanism:**
```
1. Encode current tile to JsonObject
2. Encode updateData to JsonObject
3. Merge: updateData keys override tile keys (style merged separately)
4. Decode merged JsonObject back to T via KSerializer
5. tile = decoded result
```

---

## EventManager: The Event Orchestrator

Constructs `EventRunningScope` for each event execution and delegates to `EventRunnerManager`.

**Attachment methods** (called once during `MosaicScreenStateHolder` init):
- `attachTilesEditor(TilesEditor)`
- `attachTilesOverlaysEditor(TilesOverlaysEditor)`
- `attachTilesEventHolder(TilesEventHolder)`
- `attachScreenBehaviors(ScreenBehaviorsHolder)`
- `attachDataHolder(DataHolder)`
- `attachTilesEventDispatcher(TilesEventDispatcher)`

**Execution methods:**
- `triggerEvents(trigger, data)` — finds events on root by trigger, executes them
- `runEvents(eventSchemas, data)` — executes a list
- `runEvent(eventSchema, data)` — builds scope + runs single runner

---

## EventRunnerManager: The Runner Registry

Maps `KClass<out EventSchema>` → `EventRunner<*>`. Populated from `baseEventsDefinitions + customDefinitions` in Koin.

On `runEvent()`, looks up runner by exact class, creates scope, calls `runner.runEvent(scope)`. Logs error if no runner found.

---

## TileRendererManager: The Renderer Registry

Maps `KClass<out TileSchema>` → `TileRenderer<*>`. Populated from `baseTilesDefinitions + customDefinitions` in Koin.

On `Render()`:
1. Checks `tileSchema.isGone()` — skips if true
2. Looks up renderer by class
3. Creates `TileRenderingScope` with tileId + events + onEvent callback
4. Calls `renderer.Render(tileSchema)` in the scope

---

## EventHolder: The Stateful Event Node

Mirrors `TileHolder` for events. Abstract class (always `class`).

- `event: T` — mutable schema reference
- `trigger: EventTrigger` — determines when this event fires
- `events: List<EventHolder<*>>?` — nested child events
- `tiles: List<TileHolder<*>>?` — tiles referenced by the event (e.g. AddTilesEvent holds the tiles to add)
- `get(): T` — returns updated schema with children
- `update(updateData)` — JSON merge update

---

## Definition Objects: Binding the Layers

```kotlin
// Tile Definition — binds schema class to renderer + holder builder
object ButtonTileDefinition : TileDefinition<ButtonTileSchema> {
    override val tileSchemaClass = ButtonTileSchema::class
    override val tileRenderer = ButtonTileRenderer
    override val tileHolderBuilder = ButtonTileHolderBuilder
}

// Event Definition — binds schema class to runner + holder builder
object AddTilesEventDefinition : EventDefinition<AddTilesEventSchema> {
    override val eventSchemaClass = AddTilesEventSchema::class
    override val eventRunner = AddTilesEventRunner
    override val eventHolderBuilder = AddTilesEventHolderBuilder
}
```

Definitions are consumed by Koin to populate registries: `TileRendererManager`, `TileHolderBuilderManager`, `EventRunnerManager`, `EventHolderBuilderManager`, and `MosaicSerializer`.

---

## Dependency Injection (Koin Modules)

| Module | Provides |
|---|---|
| `applicationModule` | `NavigatorsHolder`, `DataMailer`, `ScreenExtrasHolder` |
| `dataModule` | `MosaicDatabase`, `MosaicFileSystem`, `MosaicRepository`, `MosaicNetwork` |
| `serializerModule` | `MosaicSerializer` (built from all definitions) |
| `renderingModule` | `TileRendererManager`, `TileHolderBuilderManager` |
| `eventModule` | `EventRunnerManager`, `EventHolderBuilderManager` |
| `stateHolder` | `MosaicScreenStateHolder` (viewModel factory), `MosaicApplicationStateHolder` |
| `useCaseModule` | All domain use cases (Get/Update/Remove for plain and segmented data, network, file) |
| `dataProcessorsModule` | `EventRunnerDataProcessor` (bound as `DataProcessor`) |
| `platformModule` | Platform-specific: `MosaicLogger`, `DataChest`, `PlatformFileHandler` |

---

## Persistence Layer

### DataHolder (in-memory, screen-scoped)
Lives for the duration of a screen's ViewModel lifecycle. Three namespaces:
- `plainData: Map<String, Any>` — global key/value
- `segmentedData: Map<segmentId, Map<String, Any>>` — segmented key/value
- `navigationData: Map<String, Any>` — immutable, set at navigation time

### DataMailer (in-memory, global)
Simple `Map<String, Any>` shared across screens. Used by `SendData` and `CheckForReceivedData` events.

### MosaicDatabase (persistent, SQLDelight)
Suspendable API. Two namespaces: `plainData` (key-value) and `segmentedData` (segment + key-value). Platform drivers: SQLite on Android/iOS/Desktop, sql.js on Web.

### MosaicFileSystem (persistent, file I/O)
Suspendable `saveFile`, `getFile`, `deleteFile` on `ByteArray`. Platform-specific via `PlatformFileHandler` expect/actual.

### DataChest (persistent, key-value preferences)
Typed getters/setters for `Int`, `Long`, `String`, `Float`, `Boolean`. Backed by SharedPreferences (Android) / UserDefaults (iOS).

### TilesValueProducer (in-memory, tile-scoped)
Read-only datasource that exposes values from active tiles. Accessed via `EventRunningScope.tilesValueProducer`.

```kotlin
interface TilesValueProducer {
    fun getValueWithKey(tileId: String, key: String): Map<String, Any>?
}
```

Used when a `DataSourceSchema.Tile(tileId, dataKey)` is referenced in a `GetDataEvent` or `EvaluateDataEvent`. The tile identified by `tileId` must expose the requested `key` through its holder. Returns `null` if the tile or key does not exist.

---

## Style System

`StyleSchema` is attached to every `TileSchema` and controls visual presentation:

```
StyleSchema
├── size: SizeSchema         → width + height (Fill, Wrap, Fixed(dp), Weight(float))
├── margin: MarginSchema?    → external spacing (start, top, end, bottom in dp)
├── padding: PaddingSchema?  → internal spacing
├── background: ColorSchema? → fill color (#AARRGGBB or #RRGGBB)
├── border: BorderSchema?    → stroke width + color
├── clip: ClipSchema?        → shape clipping (circle, rounded, etc.)
└── windowInsets: WindowInsetsSchema? → system bars, ime, etc.
```

Applied via `Modifier.styledWith(style: StyleSchema)` in the client.

---

## Navigation Architecture

- `NavigatorsHolder` — global registry of `NavigationController` instances by `navigatorId`.
- `NavigationController` — wraps `NavBackStack<ScreenNavKey>`, supports `popUpTo` with `inclusive` flag.
- Each `NestedNavigationGraphTileSchema` declares a `navigatorId` and a list of `Entry` objects.
- `NavigateEventSchema.navigatorId` must match a registered navigator.
- Navigation data passed via `navigationData` map becomes read-only `DataHolder.getNavigationData()` in the destination screen.

---

## Broadcast System

`BroadcastChannel` is a `typealias` for `SharedFlow<BroadcastData>`. Provided via `LocalBroadcastChannel` CompositionLocal.

```kotlin
typealias BroadcastChannel = SharedFlow<BroadcastData>

interface BroadcastData {
    val tileId: String?   // null = broadcast to all; set = targeted to a tile
}
```

Tiles observe `LocalBroadcastChannel.current` to react to cross-tile signals (e.g., `ColumnTileRenderer` reacts to `ScrollToTop`, `ScrollTo(index)` broadcast data).

EventRunners emit broadcasts via `EventRunningScope.broadcastData(data)`.

---

## Data Processing Pipeline

```
GetDataEventRunner
    │  fetches data from one or more sources
    ▼
onSuccess trigger fired with data as incomingData
    │
    ▼ (if ProcessDataEvent is chained)
ProcessDataEventRunner
    │  get<DataProcessor>(named(event.processorId))
    ▼
EventRunnerDataProcessor
    │  deserializes incomingData as List<EventSchema> or EventSchema
    ▼
runEventInline(eventSchema)   →  executes events (e.g., AddTilesEventRunner)
```

`DataProcessor` is an interface. Custom processors can be registered in Koin and referenced by ID in `ProcessDataEventSchema`.

---

## Logging

`MosaicLogger` is an abstract class with level-based filtering (`DEBUG`, `INFO`, `WARNING`, `ERROR`). Implementations are platform-specific. Available via Koin DI. Use `logError()` / `log()` helpers in `EventRunningScope` and `BuilderScope`.

---

## Key Architectural Patterns

| Pattern | Where Used |
|---|---|
| MVVM | `ScreenStateHolder` exposes `StateFlow` to Compose |
| MVI | `UIEvent` as typed actions, processed by `EventManager` |
| Registry | `TileRendererManager`, `EventRunnerManager` — O(1) lookup by KClass |
| Strategy | `EventRunner` implementations — pluggable behaviors |
| Composite | `TileHolder` tree — recursive structure with uniform `get()` |
| Factory | `TileHolderBuilder`, `EventHolderBuilder` — produce holders from schemas |
| Scope | `EventRunningScope`, `TileRenderingScope`, `BuilderScope` — context injection via extension functions |
| Supervisor Coroutines | `supervisorScope {}` in all async paths — isolated failure |
| JSON Merge Update | Tile updates via `JsonObject` merge — no reflection |
| Composition over Inheritance | `TilesManager` implements 5 interfaces; `MosaicScreenStateHolder` delegates `DataHolder` |
| CompositionLocal | `LocalTileRendererManager`, `LocalBroadcastChannel` — implicit Compose context |
