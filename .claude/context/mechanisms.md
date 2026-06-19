# Mosaic — Mechanisms Reference

> Atualizado: jun/2026. Validado contra código-fonte real (EventRunningScope, TileRenderingScope, MosaicScreenStateHolder, ScreenTilesBroadcastChannel, SystemBroadcastChannel). Para usar estes mecanismos em extensões externas, consulte a skill `skill/mosaic-extension/`.

This document describes each mechanism available inside Mosaic's client runtime: what it is, what it does, what API it exposes, and how it connects to other parts of the system. Written for developers implementing new Tiles, Events, or extending the framework.

---

## 1. EventRunningScope

**What it is:** The execution context injected into every `EventRunner.runEvent()` call. It is the primary API surface for event logic.

**Where it comes from:** Created by `EventManager.runEvent()` before calling the runner.

**Full API:**

```kotlin
// --- Identity ---
val screenId: String                          // ID of the current screen
val triggerOwner: EventSchema                 // The event schema that is currently running
val incomingData: Any?                        // Data passed from the triggering event or action

// --- Tile manipulation ---
val tilesEditor: TilesEditor                   // add/remove/replace/update/wipe tiles
val tilesOverlaysEditor: TilesOverlaysEditor   // bottom sheets, dialogs
val tilesEventDispatcher: TilesEventDispatcher // dispatch TileEvent / TileGroupEvent; look up events by id
val tilesValueProducer: TilesValueProducer     // read values exposed by tiles (e.g. TextField content)

// --- Screen data ---
val dataHolder: DataHolder                     // in-memory screen-scoped data

// --- Screen behavior ---
val screenBehaviorsHolder: ScreenBehaviorsHolder  // change screen state (Success/Failure/Initial)

// --- Child event dispatch ---
suspend fun onTrigger(eventTrigger: EventTrigger, data: Any? = null)
    // Fires all child events of triggerOwner that match the trigger. Sequential, suspend.
    // `data` becomes `incomingData` for each child event.
suspend fun runEventInline(eventSchema: EventSchema, data: Any? = null)
    // Executes a specific EventSchema directly (no trigger filtering). Sequential, suspend.

// --- Async execution ---
// runEvent() is already a suspend fun — use withContext(Dispatchers.IO) directly:
//   override suspend fun EventRunningScope.runEvent(event: MyEvent) {
//       val result = withContext(Dispatchers.IO) { heavyWork() }
//       onTrigger(EventTriggers.onSuccess(), result)
//   }
// The event manager always calls runEvent from stateHolderScope (ViewModel lifetime).

// --- Broadcast (screen-scoped inter-tile) ---
fun broadcastData(data: ScreenTilesBroadcastData)
    // Emits to this screen's ScreenTilesBroadcastChannel (SharedFlow).
    // TileRenderers observing LocalScreenTilesBroadcastChannel will react.
    // Different from SystemBroadcastChannel — see section 8b.

// --- Koin DI ---
inline fun <reified T : Any> get(qualifier?: Qualifier, parameters?: ParametersDefinition): T
fun <T : Any> get(clazz: KClass<T>, qualifier?, parameters?): T
inline fun <reified T : Any> getAll(): List<T>
fun <T : Any> getAll(clazz: KClass<T>): List<T>
inline fun <reified T : Any> getOrNull(qualifier?, parameters?): T?
fun <T : Any> getOrNull(clazz: KClass<T>, qualifier?, parameters?): T?

// --- Type helpers ---
fun Any?.asMapAny(): Map<String, AnySerializable>?   // safe cast to map
fun Any?.asMapString(): Map<String, String>?          // safe cast + filter Strings only

// --- Logging ---
fun logError(throwable: Throwable, tag: String)   // tag is REQUIRED — no default
fun log(level: Level, msg: String)
```

**Usage pattern in an EventRunner:**

```kotlin
object MyEventRunner : EventRunner<MyEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: MyEventSchema) {
        tilesEditor.updateTile(event.tileId, mapOf("loading" to true))

        val result = withContext(Dispatchers.IO) {
            get<MyUseCase>().invoke(event.param)
        }
        // Back on main dispatcher after withContext
        onTrigger(EventTriggers.onSuccess(), data = result)
    }
}
```

**Connects to:** `TilesManager` (via `tilesEditor` / `tilesOverlaysEditor` / `tilesEventDispatcher` / `tilesValueProducer`), `DefaultDataHolder` (via `dataHolder`), `MosaicScreenStateHolder` (via `screenBehaviorsHolder` / `broadcastData`), `EventManager` (via `runEventInline` / `onTrigger`), Koin (via `get()`).

**Event chaining:** `onTrigger(trigger, data)` iterates `triggerOwner.events` filtered by trigger and runs each via `EventManager.runEvent()` sequentially. The `data` you pass becomes the child's `incomingData`. `runEventInline(schema, data)` does the same for a specific schema (no filter). Both are `suspend` — the entire chain runs in the ViewModel coroutine scope.

---

## 2. TileRenderingScope

**What it is:** The execution context injected into every `TileRenderer.Render()` call. It is the primary API surface for composable tile rendering.

**Where it comes from:** Created by `TileRendererManager.Render()` before calling the renderer.

**Full API:**

```kotlin
// Note: tileId and events are private — access them via tileSchema.id / tileSchema.events
val onEvent: (UIEvent) -> Unit              // internal callback; do not call directly

// --- Event dispatch ---
fun triggerEvent(trigger: EventTrigger, data: Any? = null)
    // Filters tileSchema.events by trigger, dispatches via onEvent.
    // Standard way to respond to user interactions.
fun dispatchEvent(tileEvent: TileEvent)
    // Dispatches a TileEvent (tile-specific internal event to the TileHolder).
fun dispatchGroupEvent(tileGroupEvent: TileGroupEvent)
    // Dispatches a TileGroupEvent (cross-tile; e.g., select-one for RadioButtons).

// --- Child rendering ---
@Composable fun RenderChild(tileSchema: TileSchema)
    // Renders one child tile via TileRendererManager.
@Composable fun RenderChildren(tileSchemas: ImmutableList<TileSchema>)
    // Renders multiple children in sequence. Accepts ImmutableList (kotlinx.collections.immutable).
```

**Usage pattern in a TileRenderer:**

```kotlin
object ButtonTileRenderer : TileRenderer<ButtonTileSchema> {
    @Composable
    override fun TileRenderingScope.Render(tileSchema: ButtonTileSchema) {
        Button(
            onClick = { triggerEvent(EventTriggers.onClick()) },
            enabled = tileSchema.enabled
        ) {
            Text(tileSchema.text)
        }
    }
}
```

**Connects to:** `TileRendererManager` (via `RenderChild`), `MosaicScreenStateHolder` (via `onEvent` callback chain).

---

## 3. BuilderScope

**What it is:** The context injected into every `TileHolderBuilder.build()` and `EventHolderBuilder.build()` call. Used during the construction of the `TileHolder`/`EventHolder` tree from schemas.

**Full API:**

```kotlin
// --- Construction ---
fun buildTileHolder(tileSchema: TileSchema): TileHolder<*>
fun buildEventHolder(eventSchema: EventSchema): EventHolder<*>
fun List<TileSchema>.buildTileHolders(): MutableList<TileHolder<*>>
fun List<EventSchema>.buildEventHolders(): MutableList<EventHolder<*>>

// --- Deserialization ---
inline fun <reified T : Any> decode(data: Any): T
fun <T> decode(strategy: KSerializer<T>, data: Any): T
inline fun <reified T : Any> decodeOrNull(data: Any?): T?

// --- Koin DI ---
inline fun <reified T : Any> get(qualifier?, parameters?): T
fun <T : Any> getOrNull(...): T?

// --- Logging ---
fun logError(tag: String, throwable: Throwable)
fun log(level: Level, msg: String)
```

**Usage pattern in a TileHolderBuilder:**

```kotlin
object ColumnTileHolderBuilder : TileHolderBuilder<ColumnTileSchema, ColumnTileHolder> {
    override fun BuilderScope.build(tileModel: ColumnTileSchema) =
        ColumnTileHolder(
            id = tileModel.id,
            tile = tileModel,
            tiles = tileModel.tiles?.buildTileHolders(),
            events = tileModel.events?.buildEventHolders()
        )
}
```

**Connects to:** `TileHolderBuilderManager`, `EventHolderBuilderManager`, `MosaicSerializer`, Koin.

---

## 4. TileHolder

**What it is:** The stateful runtime representation of a `TileSchema`. Lives in the `TilesManager` tree. Always a `class` (stateful), never `object`.

**Key fields:**

```kotlin
abstract val id: String                          // unique tile ID
abstract var tile: T                             // mutable schema reference
abstract val events: MutableList<EventHolder<*>>?
abstract val tiles: MutableList<TileHolder<*>>?
```

**Key methods:**

```kotlin
abstract fun get(): T
    // Returns the current schema with all children serialized into it.
    // Called by TilesManager when rebuilding state to pass to Compose.

fun UpdateScope.update(updateData: Map<String, Any?>)
    // Applies a partial JSON merge update to `tile`. Only the keys present in
    // updateData are overwritten. Style is merged separately.

open fun getTileHolder(tileId: String, includeEventsOnSearch: Boolean = true): TileHolder<*>?
    // Depth-first recursive search. Searched order: self → tiles → events.tiles

open fun getEventsByTrigger(eventTrigger: EventTrigger): List<EventSchema>?
    // Returns all EventSchemas from `events` whose trigger matches.

open fun TileEventScope.onTileEvent(event: TileEvent)
    // Override to handle tile-specific TileEvents (e.g., ExpandableTile toggle).

open fun TileEventScope.onTileGroupEvent(event: TileGroupEvent)
    // Override to handle group events (e.g., select-one across RadioButtons).
```

**Child manipulation (used by TilesManager):**

```kotlin
fun addChild(child: TileHolder<*>, where: InsertionPosition = End)
fun addChildren(children: List<TileHolder<*>>, where: InsertionPosition = End)
fun removeChild(id: String)
fun removeChildren(ids: List<String>)
fun wipeChildren()
```

---

## 5. EventHolder

**What it is:** The stateful runtime representation of an `EventSchema`. Lives inside `TileHolder.events`. Always a `class` (stateful), never `object`.

**Key fields:**

```kotlin
abstract val id: String
abstract var event: T
abstract val trigger: EventTrigger
abstract val events: List<EventHolder<*>>?   // nested child events
abstract val tiles: List<TileHolder<*>>?     // tiles embedded in the event (e.g. AddTilesEvent)
```

**Key methods:**

```kotlin
abstract fun get(): T
    // Returns current schema with children.

fun UpdateScope.update(updateData: Map<String, Any?>)
    // JSON merge update on `event`.

fun getEventHolder(eventId: String): EventHolder<*>?
fun getTileHolder(tileId: String): TileHolder<*>?
```

---

## 6. DataHolder

**What it is:** In-memory, screen-scoped key-value storage. Lives in `MosaicScreenStateHolder`. Cleared when the ViewModel is destroyed.

**Three namespaces:**

| Namespace | Key type | Scope | Mutable |
|---|---|---|---|
| `plainData` | `String` | Screen-wide | Yes |
| `segmentedData` | `segmentId + String` | Per-segment | Yes |
| `navigationData` | `String` | Set at navigate time | **Read-only** |

**Full API (available in `EventRunningScope.dataHolder`):**

```kotlin
fun addPlainData(data: Any, dataId: String)
fun addSegmentedData(data: Any, segmentId: String, dataId: String)
fun removePlainData(dataId: String)
fun removeSegmentedData(segmentId: String, dataId: String)
fun getPlainData(dataId: String): Any?
fun getAllPlainData(): Map<String, Any>
fun getSegmentedData(dataId: String, segmentId: String): Any?
fun getAllSegmentedData(segmentId: String): Map<String, Any>
fun getNavigationData(dataId: String): Any?
fun getAllNavigationData(): Map<String, Any>
fun wipePlainData()
fun wipeSegmentedData(segmentId: String)
fun wipeSegmentedData()       // wipes all segments
```

**Used by:** `GetDataEventRunner`, `UpdateDataEventRunner`, `RemoveDataEventRunner`, `EvaluateDataEventRunner`.

---

## 7. DataMailer

**What it is:** Global in-memory key-value store for cross-screen data passing. Singleton in Koin (`applicationModule`).

**API:**

```kotlin
fun sendData(dataKey: String, data: Any)
fun getData(dataKey: String): Any?
```

**Used by:** `SendDataEventRunner` (writes), `CheckForReceivedDataEventRunner` (reads). Does not survive process death.

---

## 8. ScreenTilesBroadcastChannel

**What it is:** A screen-scoped `SharedFlow<ScreenTilesBroadcastData>` for **reactive cross-tile communication within a screen**. Created by `MosaicScreenStateHolder`, provided via `LocalScreenTilesBroadcastChannel` CompositionLocal.

```kotlin
class ScreenTilesBroadcastChannel {
    val channel: SharedFlow<ScreenTilesBroadcastData>
    suspend fun broadcast(data: ScreenTilesBroadcastData)
}

interface ScreenTilesBroadcastData {
    val tileId: String?   // null = broadcast to all tiles; non-null = targeted to specific tile
}
```

**Emitting (from EventRunners):**

```kotlin
// EventRunningScope.broadcastData() delegates to ScreenBehaviorsHolder.broadcastData()
// which calls screenBroadcastChannel.broadcast(data) on stateHolderScope
broadcastData(ColumnTileScreenTilesBroadcastData.ScrollToTop(tileId = "my_column", smoothly = true))
```

**Consuming (in TileRenderers):**

```kotlin
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalScreenTilesBroadcastChannel

@Composable
override fun TileRenderingScope.Render(tileSchema: ColumnTileSchema) {
    val broadcastChannel = LocalScreenTilesBroadcastChannel.current
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        broadcastChannel.channel.collect { data ->
            if (data.tileId == null || data.tileId == tileSchema.id) {
                when (data) {
                    is ColumnTileScreenTilesBroadcastData.ScrollToTop ->
                        if (data.smoothly) listState.animateScrollToItem(0)
                        else listState.scrollToItem(0)
                    is ColumnTileScreenTilesBroadcastData.ScrollTo ->
                        listState.animateScrollToItem(data.index)
                }
            }
        }
    }
}
```

**Connects to:** `MosaicScreenStateHolder` (owns the `MutableSharedFlow`, emits via `broadcastData()`), `MosaicScreen` (provides via `LocalScreenTilesBroadcastChannel`).

> **⚠️ Do not confuse with `SystemBroadcastChannel`** — see section 8b below. They are completely separate mechanisms.

---

## 8b. SystemBroadcastChannel

**What it is:** An **app-scoped** broadcast channel for triggering events on tiles by a string `broadcastId`. Singleton in Koin (`applicationModule`). Completely separate from `ScreenTilesBroadcastChannel`.

```kotlin
class SystemBroadcastChannel {
    val channel: SharedFlow<SystemBroadcastData>
    suspend fun broadcast(broadcastId: String, data: AnySerializable)
}

data class SystemBroadcastData(
    val broadcastId: String,
    val data: AnySerializable,
)
```

**Who emits:** `BroadcastToSystemEventRunner` — calls `get<SystemBroadcastChannel>().broadcast(broadcastId, data)`.

**Who consumes:** `ScreenTileRenderer` (or tiles) collects `SystemBroadcastChannel.channel` and calls `EventManager.triggerEvents(OnSystemBroadcastEventTrigger(broadcastId), data)`. Tiles with `trigger = OnSystemBroadcastEventTrigger(broadcastId)` then execute.

**How it differs from ScreenTilesBroadcastChannel:**

| | `ScreenTilesBroadcastChannel` | `SystemBroadcastChannel` |
|---|---|---|
| Scope | Per-screen | App-global (singleton) |
| Identity | `tileId: String?` | `broadcastId: String` |
| Who emits | `EventRunningScope.broadcastData()` | `BroadcastToSystemEventRunner` |
| Who consumes | TileRenderers via `LocalScreenTilesBroadcastChannel` | Tiles with `OnSystemBroadcastEventTrigger` |
| Purpose | UI commands (scroll, visual state) | Server-driven tile event invocation |

---

## 9. DataProcessor

**What it is:** A pluggable processing unit that transforms incoming data during event execution. Identified by a string `id`.

**Interface:**

```kotlin
interface DataProcessor {
    val id: String
    fun EventRunningScope.proccess(data: Any)
}
```

**Built-in implementation: `EventRunnerDataProcessor`**
- `id = "EVENT_RUNNER"`
- Deserializes `data` as `List<EventSchema>` (JsonArray) or single `EventSchema` (JsonObject)
- Executes each via `runEventInline()`

**Used by `ProcessDataEventRunner`:**

```kotlin
object ProcessDataEventRunner : EventRunner<ProcessDataEventSchema> {
    override fun EventRunningScope.runEvent(event: ProcessDataEventSchema) {
        val processor = get<DataProcessor>(named(event.processorId))
        with(processor) { proccess(incomingData ?: return) }
    }
}
```

Custom processors are registered in Koin as `bind<DataProcessor>()` and referenced in `ProcessDataEventSchema.processorId`.

---

## 10. NavigatorsHolder

**What it is:** Global registry of `NavigationController` instances. Singleton in Koin (`applicationModule`).

**API:**

```kotlin
fun registerNavigator(navigatorId: String, navigationController: NavigationController)
fun unregisterNavigator(navigatorId: String)
operator fun get(navigatorId: String): NavigationController?
```

**`NavigationController` API:**

```kotlin
fun navigate(
    destination: String,
    navigationData: Map<String, Any> = emptyMap(),
    poppingUpTo: PoppingUpTo? = null
)
fun navigateUp()
```

**Used by:** `NavigateEventRunner`, `NavigateUpEventRunner`. Each `NestedNavigationGraphTileRenderer` registers its controller on composition and unregisters on dispose.

---

## 11. ScreenBehaviorsHolder

**What it is:** Interface exposing screen-level state transitions and broadcast emission. Implemented by `MosaicScreenStateHolder`.

**API (available in `EventRunningScope.screenBehaviorsHolder`):**

```kotlin
fun setState(state: ScreenBehaviorsHolder.State)
fun broadcastData(data: ScreenTilesBroadcastData)

sealed interface State {
    data class Success(val screenModel: ScreenModel) : State  // screenModel.tiles loaded from /screens/{id}
    data object Failure : State
    data object Initial : State
}
```

**Used by:** `ChangeScreenStateEventRunner`, `GetScreenEventRunner`, `RefreshScreenEventRunner`. Setting `Failure` swaps the tile tree to `failureTiles` (from `GraphResponse.Entry`). Setting `Success(screenModel)` replaces tiles with `screenModel.tiles` (from `ScreenResponse`). The `broadcastData()` method delegates to `MosaicScreenStateHolder.screenBroadcastChannel`.

---

## 12. TilesEditor

**What it is:** The tile manipulation API exposed through `EventRunningScope.tilesEditor`. Implemented by `TilesManager`.

**Full API:**

```kotlin
// Add to root
fun addTile(tileSchema: TileSchema, where: InsertionPosition = End)
fun addTiles(tileSchemas: List<TileSchema>, where: InsertionPosition = End)

// Add to a specific grouping tile
fun addTile(tileSchema: TileSchema, groupingTileId: String, where: InsertionPosition = End)
fun addTiles(tileSchemas: List<TileSchema>, groupingTileId: String, where: InsertionPosition = End)

// Remove
fun removeTile(tileId: String, groupingTileId: String? = null)
fun removeTiles(tileIds: List<String>, groupingTileId: String? = null)

// Replace all children of a group (wipe + add)
fun replaceTiles(tileSchemas: List<TileSchema>, groupingTileId: String? = null)

// Wipe all children of a group
fun wipeTiles(groupingTileId: String)

// Partial property update
fun updateTile(tileId: String, updateData: Map<String, Any?>)

// Inspection
fun checkIfTileHasChildren(groupingTileId: String, childrenIds: List<String>): Boolean
fun getTileChildrenCount(groupingTileId: String): Int?

// Note: all mutation methods return Result<Unit>. On failure (e.g. tile not found),
// the Result wraps TileNotFoundException or TileNotGroupingException.
```

`InsertionPosition` variants: `Start`, `End`, `Before(tileId)`, `After(tileId)`.

Every call triggers `updateState()` → Compose recomposition.

---

## 13. TilesOverlaysEditor

**What it is:** API for managing overlays (bottom sheets, dialogs). Exposed via `EventRunningScope.tilesOverlaysEditor`. Implemented by `TilesManager`.

**API:**

```kotlin
fun setBottomSheetTiles(tileSchemas: List<TileSchema>)
fun setDialogTiles(tileSchemas: List<TileSchema>)
```

Clearing overlays is done by passing an empty list, or by `ScreenTileHolder` automatically handling `TileEvent.Close`.

**Used by:** `DisplayBottomSheetEventRunner`, `DismissBottomSheetEventRunner`, `DisplayDialogEventRunner`, `DismissDialogEventRunner`.

---

## 13b. TilesValueProducer

**What it is:** Read-only API for reading values **exposed by specific tiles** (e.g., the current text in a TextField, the current scroll position). Accessed via `EventRunningScope.tilesValueProducer`.

```kotlin
interface TilesValueProducer {
    fun getValueWithKey(tileId: String, key: String): Map<String, Any>?
}
```

**Used by:** `GetDataEventRunner` when the `DataSourceSchema` is `DataSourceSchema.Tile(tileId, dataKey)`. Each tile that exposes values implements `TileEventScope.onTileEvent(TileEvent.ProduceValue)`.

**Example:** `TextFieldTileHolder` stores `"text" → currentText` on every `OnTextChanged` event. `GetData(source = DataSourceSchema.Tile("my_field", "text"))` retrieves it.

**Note:** Write-back (`UpdateData`, `RemoveData`) does not support `DataSourceSchema.Tile`.

---

## 14. TileEventScope

**What it is:** Context for `TileHolder.onTileEvent()` and `onTileGroupEvent()` overrides. Provides construction capabilities.

```kotlin
class TileEventScope(val builderScope: BuilderScope) {
    fun buildTileHolder(tileSchema: TileSchema): TileHolder<*>
    fun buildEventHolder(eventSchema: EventSchema): EventHolder<*>
    fun List<TileSchema>.buildTileHolders(): MutableList<TileHolder<*>>
    fun List<EventSchema>.buildEventHolders(): MutableList<EventHolder<*>>
}
```

Used when a `TileHolder` needs to construct child holders in response to a `TileEvent` (e.g., `ExpandableTileHolder` builds child tiles on expand).

---

## 15. UpdateScope

**What it is:** Minimal context for `TileHolder.update()` and `EventHolder.update()`. Carries only the serializer needed for JSON merge.

```kotlin
class UpdateScope(val serializer: MosaicSerializer)
```

The update mechanism:
1. Encodes current schema to `JsonObject`
2. Encodes `updateData: Map<String, Any?>` to `JsonObject`
3. Merges (updateData keys override schema keys; style field is deep-merged)
4. Decodes merged `JsonObject` back to `T` via `KSerializer`

This is the only way to mutate a tile's schema at runtime without replacing the entire tile.

---

## 16. MosaicSerializer

**What it is:** Central polymorphic serializer for all `TileSchema` and `EventSchema` subclasses. Singleton in Koin.

**Used by:** `BuilderScope` (decode), `TileHolder.update()` / `EventHolder.update()` (encode + decode), `EventRunnerDataProcessor` (decode events from data).

Every new `TileSchema` or `EventSchema` must be registered in `MosaicSerializer`'s polymorphic blocks. Omitting this causes silent runtime deserialization failure.

---

## 17. DataSourceSchema (Enum for Data Targeting)

Used in data events (`GetData`, `UpdateData`, `RemoveData`) to specify which storage layer to target:

| Variant | Storage | Scope |
|---|---|---|
| `ScreenPlainData` | `DataHolder.plainData` | Current screen, in-memory |
| `ScreenSegmentedData(segmentId)` | `DataHolder.segmentedData` | Current screen, in-memory |
| `ScreenNavigationData` | `DataHolder.navigationData` | Current screen, **read-only** |
| `PlainDataBase` | `MosaicDatabase` plain table | Persistent, all screens |
| `SegmentedDataBase(segmentId)` | `MosaicDatabase` segmented table | Persistent, all screens |
| `Tile(tileId, dataKey)` | `TilesValueProducer` (tile-exposed values) | Read-only; only `GetData` |

---

## 18. AccessModeSchema (Enum for Data Granularity)

Used in `GetData` and `RemoveData` to specify how many records to operate on:

| Variant | Meaning |
|---|---|
| `Full` | All records in the target namespace |
| `Single(dataId)` | Exactly one record by ID; fails if missing |
| `Batch(dataIds, allowMissingData, unwrapValuesToList)` | Multiple records by ID list |

`Batch.allowMissingData = true` skips missing keys silently.
`Batch.unwrapValuesToList = true` returns only values (not keys) as a list.

---

## 19. ScreenStateHolder (ViewModel Base)

**What it is:** Abstract ViewModel base class. `MosaicScreenStateHolder` extends it.

**Lifecycle hooks (override in subclass):**

```kotlin
protected open fun onFirstDisplay()   // called once on first Compose appearance
protected open fun onDisplay()        // called every time screen appears
protected open fun onPause()          // screen goes background
protected open fun onStop()           // ViewModel cleared (scope cancelled)
```

**Reactive state:**

```kotlin
abstract val internalUIState: MutableStateFlow<State>
val uiState: StateFlow<State> get() = internalUIState.asStateFlow()

val internalEffects = MutableSharedFlow<Effect>()
val effects: SharedFlow<Effect> get() = internalEffects.asSharedFlow()

// Helper to emit effects:
fun MutableSharedFlow<Effect>.dispatch(effect: Effect)
```

**Coroutine scopes:**

```kotlin
val stateHolderScope: CoroutineScope
    // Created lazily. Backed by SupervisorJob + Main dispatcher (or EmptyCoroutineContext as fallback).
    // Cancelled on onStop() / onCleared().
```

**Lifecycle binding (call in screen Composable):**

```kotlin
@Composable fun bindScreenLifecycle()
    // Uses DisposableEffect to call onDisplay()/onFirstDisplay() on composition
    // and onPause()/onStop() on disposal.
```
