# Mosaic — Architecture & Data Flow

## Module Dependency Graph

```
mosaic-core  (no Mosaic deps)
    ↑
    ├── mosaic-client   (deserializes + renders)
    ├── mosaic-server   (DSL + serializes)
    ├── sample-client   (consumes client + core)
    └── sample-server   (consumes server + core)
```

`mosaic-core` is the single source of truth for all contracts (Schemas). Neither client nor server can depend on each other.

---

## Full Execution Pipeline

### 1. Backend: DSL → JSON

```
Graph(startEntryId = "home") {
  entry("home") {
    Column {
      Button("Go") { Navigate(..., trigger = OnClickEventTrigger()) }
    }
  }
}
    ↓
GraphBuilder.build() → GraphResponse
    ↓
Kotlinx Serialization → JSON (polymorphic, @SerialName discriminators)
```

### 2. HTTP Transfer

```
GET /graph → JSON payload
{
  "startEntryId": "home",
  "entries": [{
    "screenId": "home",
    "initialTiles": [{ "type": "Column", "id": "col1", "tiles": [...] }],
    "initialEvents": [{ "type": "GetScreen", ... }]
  }]
}
```

### 3. Client: JSON → UI

```
JSON
  ↓ MosaicSerializer.decode() → GraphResponse
  ↓ MosaicApplicationStateHolder.setupScreenExtras()
      → stores entries + transitions in ScreenExtrasHolder

NavDisplay shows start screen
  ↓ MosaicScreen("home") composable
      → creates MosaicScreenStateHolder (ViewModel)
          → TilesManager.setup(initialTiles, initialEvents)
              → TileHolderBuilderManager builds TileHolder tree
              → EventHolderBuilderManager builds EventHolder tree
          → calls onUpdateStateRequest(rootTile)
  ↓ TileRendererManager.Render(rootTile)
      → recursively dispatches to TileRenderer for each schema type
      → Compose renders UI
```

### 4. Event Execution

```
User clicks Button
  ↓ TileRenderer calls onEvent callback
  ↓ MosaicScreenStateHolder.onEvent(TileEventHolderUIEvent)
  ↓ TilesManager.onEvent(tileId, event)
  ↓ TileHolder.onTileEvent() → finds matching EventHolder by trigger
  ↓ EventManager.triggerEvents(OnClickEventTrigger)
  ↓ EventRunnerManager dispatches to EventRunner
  ↓ EventRunner.runEvent(event) in EventRunningScope
      → may: navigate, update tiles, show overlay, run network request, etc.
  ↓ Any tile mutations → TilesManager.updateState() → rootTile re-emitted
  ↓ MosaicScreen recomposes
```

---

## Key Design Patterns

### Polymorphic Serialization
All `TileSchema` and `EventSchema` subclasses are registered in `MosaicSerializer` using a `type` JSON discriminator:
```json
{ "type": "Button", "id": "btn1", "text": "Click me", ... }
```
Any new schema MUST be added to `MosaicSerializer` or deserialization will fail silently.

### Registry Pattern
- `TileHolderBuilderManager`: maps `KClass<out TileSchema>` → `TileHolderBuilder`
- `TileRendererManager`: maps `KClass<out TileSchema>` → `TileRenderer`
- `EventRunnerManager`: maps `KClass<out EventSchema>` → `EventRunner`
- `EventHolderBuilderManager`: maps `KClass<out EventSchema>` → `EventHolderBuilder`

All registrations happen in `MosaicModules` (Koin DI).

### Hierarchical Tile Tree
```
ScreenTileHolder (id = "mosaic::root")
  └── ColumnTileHolder
        ├── ButtonTileHolder
        │     └── NavigateEventHolder
        └── TextTileHolder
```

Events are children of TileHolders. Tile mutations propagate up to root and trigger recomposition.

### Event Chaining
```kotlin
SendNetworkRequest(...) {
    UpdateTiles(trigger = OnStartEventTrigger(), ...)   // fires immediately
    AddTiles(trigger = OnSuccessEventTrigger(), ...)    // fires after 2xx
    DisplaySnackbar(trigger = OnFailureEventTrigger())  // fires on error
}
```
Each event in `events` is a child event. `onTrigger(trigger, data)` in a Runner dispatches to matching children.

### Nested Navigation
Multiple navigators can exist simultaneously:
- Root navigator: `"main"` (managed by `MosaicApplicationStateHolder`)
- Nested navigators: any `navigatorId` in `NestedNavigationGraphTileSchema` or `AdaptiveNavigationTileSchema`

Navigate to a nested screen: `Navigate(destination = "tab-detail", navigatorId = "inner-nav")`

---

## Screen State Machine

Each screen has three states managed by `MosaicScreenStateHolder`:

```
Initial ──[GetScreen success]──→ Displaying(rootTile)
Initial ──[GetScreen failure]──→ Failure(failureTiles)
Failure ──[ChangeScreenState]──→ Initial (retry)
Displaying ──[ChangeScreenState]──→ Failure / Initial / Success(newTiles)
```

Tiles and events differ per state: `initialTiles/Events`, `failureTiles/Events`, `success tiles/events`.
