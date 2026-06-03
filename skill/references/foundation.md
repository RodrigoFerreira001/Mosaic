# Mosaic — Client Foundation

The foundation is the engine that runs on the client. Backend developers rarely need this level of detail, but it's essential when adding new Tiles, Events, or debugging runtime behavior.

---

## MosaicApplication — Entry Point

```kotlin
@Composable
fun MosaicApplication(
    applicationId: String,
    baseUrl: String,
    additionalKoinModule: Module = module { },
    logger: MosaicLogger = DefaultMosaicLogger(),
    tileDefinitions: List<TileDefinition<out TileSchema>> = emptyList(),
    eventDefinitions: List<EventDefinition<out EventSchema>> = emptyList(),
    drawableResources: Map<String, DrawableResource> = emptyMap(),
    appSplash: @Composable BoxScope.() -> Unit,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MosaicTypography(),
    materialSymbolFontsConfig: MaterialSymbolFontsConfig = MaterialSymbolFontsConfig()
)
```

- Initializes Koin with `MosaicModules` + `additionalKoinModule`
- Custom tiles/events are passed as `tileDefinitions` / `eventDefinitions`
- Custom drawable resources are passed as `drawableResources` (keyed by `resourceName` used in `ImageTileSchema`)
- Wraps app in Material3 theme

---

## MosaicModules — Dependency Injection

Koin modules configured in `mosaic-client/.../di/MosaicModules.kt`:

| Module | Contents |
|---|---|
| `applicationModule` | `ApplicationID`, `ScreenExtrasHolder`, `NavigatorsHolder`, `DataMailer`, `Logger`, `DrawableResourcesHolder` |
| `dataModule` | Network client, SQLDelight databases, file system, repositories |
| `serializerModule` | `MosaicSerializer` with all registered tile/event types |
| `renderingModule` | `TileRendererManager`, `TileHolderBuilderManager` |
| `eventModule` | `EventRunnerManager`, `EventHolderBuilderManager` |
| `stateHolder` | ViewModels: `MosaicApplicationStateHolder`, `MosaicScreenStateHolder` |
| `useCaseModule` | Use cases for screen fetch, network, data, files, permissions |
| `platformModule` | Platform-specific implementations (expect/actual) |

**Adding a new Tile/Event:** register its `Definition` in `MosaicModules` by adding it to the `tileDefinitions` or `eventDefinitions` list.

---

## TilesManager — Tile Orchestration

Central hub for all tile tree operations. Implements 5 interfaces simultaneously:

| Interface | Purpose |
|---|---|
| `TilesEditor` | add/remove/replace/wipe/update tiles |
| `TilesStateUpdater` | emit new UI state when tiles change |
| `TilesEventDispatcher` | route UI events to the correct TileHolder |
| `TilesOverlaysEditor` | set/dismiss dialog, bottom sheet content |
| `TilesEventHolder` | query events by trigger (used by EventManager) |
| `TilesValueProducer` | extract data from tiles (used by GetData runner) |

Maintains a hierarchy rooted at `ScreenTileHolder` (id = `"mosaic::root"`).

```kotlin
// Key operations:
tilesManager.setup(tiles, navDrawerTiles, events, onUpdateStateRequest)
tilesManager.addTiles(tileSchemas, groupingTileId)
tilesManager.removeTiles(tileIds, groupingTileId)
tilesManager.replaceTiles(tileSchemas, groupingTileId)
tilesManager.updateTile(tileId, data)       // JSON-patch mechanism
tilesManager.wipeTiles(groupingTileId)
```

---

## TileHolder — Stateful Tile Wrapper

Abstract class. Each tile type has its own concrete `Holder`.

```kotlin
abstract class TileHolder<T : TileSchema> {
    abstract val id: String
    abstract val tile: T
    abstract val events: List<EventHolder<*>>?
    abstract val tiles: List<TileHolder<*>>?   // null for leaf tiles

    fun getTileHolder(tileId: String): TileHolder<*>?
    fun getEventHolder(eventId: String): EventHolder<*>?
    fun getEventsByTrigger(trigger: EventTrigger): List<EventHolder<*>>
    fun update(updateData: Map<String, Any?>)   // JSON-patches the schema
    fun addChild(tileHolder) / removeChild(tileId)
}
```

**Update mechanism:** converts `updateData` map → JSON → merges with current tile JSON → deserializes back to `TileSchema`. This is how `UpdateTiles` works at runtime.

---

## EventManager — Event Dispatch

Coordinates event execution per screen. Created by `MosaicScreenStateHolder`.

```kotlin
eventManager.triggerEvents(trigger: EventTrigger, data: Any?)
// → searches TileHolder tree for events matching trigger
// → for each match, creates EventRunningScope and calls EventRunner
```

**EventRunningScope** is instantiated fresh per event execution, containing:
- References to `tilesEditor`, `tilesOverlaysEditor`, `tilesValueProducer`, `dataHolder`, `screenBehaviorsHolder`
- `incomingData` — data passed from the parent event's trigger
- `screenId`, `triggerOwner`
- Coroutine scope methods: `runSuspendOnScreenScope`, `runSuspendOnStateHolderScope`

---

## MosaicScreenStateHolder — Screen ViewModel

Created per screen instance. Manages:
- Screen state: `Initial`, `Failure`, `Displaying(rootTile)`
- `TilesManager` — tile tree
- `EventManager` — event dispatch
- `TileRendererManager` — renderer registry
- `BroadcastChannel` — pub/sub for cross-tile communication

**UI Event routing:**
```kotlin
UIEvent.TileEventHolderUIEvent   → tilesManager.onEvent(tileId, event)
UIEvent.TileGroupEventHolderUIEvent → tilesManager.onGroupEvent(event)
UIEvent.EventSchemaHolderUIEvent → eventManager.runEvents(eventSchemas, data)
```

---

## OverlayContainer — Overlay System

Wraps each screen with 4 overlay types via CompositionLocals:

| Overlay | CompositionLocal | Managed by |
|---|---|---|
| Bottom Sheet | `LocalBottomSheetState` | `tilesOverlaysEditor` |
| Dialog | `LocalDialogState` | `tilesOverlaysEditor` |
| Navigation Drawer | `LocalNavigationDrawerState` | `broadcastData()` |
| Snackbar | `LocalSnackBarState` | `broadcastData()` |

Overlay content (tiles) is set by event runners. The container observes state and renders accordingly.

---

## Definition Pattern (Tile Example)

Every tile type needs 4 objects in `mosaic-client`:

```kotlin
// 1. Definition — binds KClass to Renderer + HolderBuilder
object MyTileDefinition : TileDefinition<MyTileSchema>(
    schemaClass = MyTileSchema::class,
    renderer = MyTileRenderer,
    holderBuilder = MyTileHolderBuilder
)

// 2. Renderer — composable UI
object MyTileRenderer : TileRenderer<MyTileSchema> {
    @Composable
    override fun TileRenderingScope.Render(tileSchema: MyTileSchema) {
        TODO("Implement UI")
    }
}

// 3. Holder — stateful wrapper
class MyTileHolder(override val tile: MyTileSchema) : TileHolder<MyTileSchema>() {
    override val id = tile.id
    override val events = tile.events?.map { /* build event holder */ }
    override val tiles = null  // null if leaf tile
}

// 4. HolderBuilder — factory
object MyTileHolderBuilder : TileHolderBuilder<MyTileSchema, MyTileHolder> {
    override fun BuilderScope.build(tileModel: MyTileSchema) = MyTileHolder(tileModel)
}
```

Register in `MosaicModules` by adding `MyTileDefinition` to the definitions list.

---

## GraphUIState & Navigation

```kotlin
data class GraphUIState(
    val entries: List<Entry>,
    val startEntryId: String,
    val defaultTransition: ContentTransitionSchema?,
    val defaultPopTransition: ContentTransitionSchema?,
    val defaultPredictivePopTransition: ContentTransitionSchema?
)
```

Built from the `GraphResponse` during app initialization. Drives `NavDisplay` with `NavBackStack<ScreenNavKey>`. Each screen push creates a new `MosaicScreen` composable with its own `MosaicScreenStateHolder`.
