# Mosaic — Mechanisms Reference

This file describes Mosaic's runtime mechanisms for developers implementing **custom EventRunners, TileRenderers, TileHolders, and EventHolders** (client-side extension work). Server-side DSL authors only need sections 1, 6, 7, 12.

---

## 1. EventRunningScope

Execution context injected into every `EventRunner.runEvent()` call.

```kotlin
// Identity
val screenId: String
val triggerOwner: EventSchema
val incomingData: Any?           // data from the triggering event

// Tile manipulation
val tilesEditor: TilesEditor
val tilesOverlaysEditor: TilesOverlaysEditor
val tilesEventDispatcher: TilesEventDispatcher
val tilesValueProducer: TilesValueProducer

// Screen data
val dataHolder: DataHolder

// Screen behavior
val screenBehaviorsHolder: ScreenBehaviorsHolder

// Child event dispatch
suspend fun onTrigger(eventTrigger: EventTrigger, data: Any? = null)
    // Fires all child events matching the trigger. data becomes incomingData.
suspend fun runEventInline(eventSchema: EventSchema, data: Any? = null)
    // Executes a specific EventSchema directly.

// Screen-scoped broadcast
fun broadcastData(data: ScreenTilesBroadcastData)

// Koin DI
inline fun <reified T : Any> get(...): T
inline fun <reified T : Any> getOrNull(...): T?

// Type helpers
fun Any?.asMapAny(): Map<String, AnySerializable>?
fun Any?.asMapString(): Map<String, String>?

// Logging
fun logError(throwable: Throwable, tag: String)
fun log(level: Level, msg: String)
```

**Async pattern:**
```kotlin
override suspend fun EventRunningScope.runEvent(event: MyEvent) {
    val result = withContext(Dispatchers.IO) { /* blocking work */ }
    onTrigger(EventTriggers.onSuccess(), result)
}
```

---

## 2. TileRenderingScope

Execution context for `TileRenderer.Render()` — client-side only.

```kotlin
fun triggerEvent(trigger: EventTrigger, data: Any? = null)  // user interactions
fun dispatchEvent(tileEvent: TileEvent)                      // internal tile event
fun dispatchGroupEvent(tileGroupEvent: TileGroupEvent)       // cross-tile group event

@Composable fun RenderChild(tileSchema: TileSchema)
@Composable fun RenderChildren(tileSchemas: ImmutableList<TileSchema>)
```

---

## 3. BuilderScope

Injected into `TileHolderBuilder.build()` and `EventHolderBuilder.build()` — client-side only.

```kotlin
fun buildTileHolder(tileSchema: TileSchema): TileHolder<*>
fun buildEventHolder(eventSchema: EventSchema): EventHolder<*>
fun List<TileSchema>.buildTileHolders(): MutableList<TileHolder<*>>
fun List<EventSchema>.buildEventHolders(): MutableList<EventHolder<*>>
inline fun <reified T : Any> decode(data: Any): T
inline fun <reified T : Any> decodeOrNull(data: Any?): T?
```

---

## 4. TileHolder

Stateful runtime representation of a `TileSchema`. Always `class`, never `object`.

```kotlin
abstract val id: String
abstract var tile: T
abstract val events: MutableList<EventHolder<*>>?
abstract val tiles: MutableList<TileHolder<*>>?
abstract fun get(): T    // returns schema with all children's current state

fun UpdateScope.update(updateData: Map<String, Any?>)  // JSON merge update
fun getTileHolder(tileId: String): TileHolder<*>?       // recursive search
fun getEventsByTrigger(trigger: EventTrigger): List<EventSchema>?

// Child manipulation
fun addChild(child: TileHolder<*>, where: InsertionPosition = End)
fun removeChild(id: String)
fun wipeChildren()
```

---

## 5. EventHolder

Stateful runtime representation of an `EventSchema`. Always `class`, never `object`.

```kotlin
abstract val id: String
abstract var event: T
abstract val trigger: EventTrigger
abstract val events: List<EventHolder<*>>?
abstract val tiles: List<TileHolder<*>>?
abstract fun get(): T
fun UpdateScope.update(updateData: Map<String, Any?>)
```

---

## 6. DataHolder

In-memory, screen-scoped key-value storage. Accessed via `EventRunningScope.dataHolder`.

Three namespaces:
- `plainData` — flat key-value, screen-scoped
- `segmentedData` — per-segment key-value, screen-scoped
- `navigationData` — set at navigate time, **read-only**

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
fun wipeSegmentedData()  // all segments
```

---

## 7. DataMailer

Global singleton for cross-screen data passing.

```kotlin
fun sendData(dataKey: String, data: Any)
fun getData(dataKey: String): Any?
```

Used by `SendData` (writes) and `CheckForReceivedData` (reads). Does not survive process death.

---

## 8. ScreenTilesBroadcastChannel

Screen-scoped `SharedFlow` for reactive cross-tile communication within a screen.

```kotlin
class ScreenTilesBroadcastChannel {
    val channel: SharedFlow<ScreenTilesBroadcastData>
    suspend fun broadcast(data: ScreenTilesBroadcastData)
}

interface ScreenTilesBroadcastData {
    val tileId: String?  // null = all tiles; non-null = targeted
}
```

**Emit** from EventRunner: `EventRunningScope.broadcastData(data)` — delegates to this channel.
**Consume** in TileRenderer: `LocalScreenTilesBroadcastChannel.current.channel.collect { ... }`

---

## 8b. SystemBroadcastChannel

App-scoped singleton for cross-screen event triggering.

```kotlin
class SystemBroadcastChannel {
    val channel: SharedFlow<SystemBroadcastData>
    suspend fun broadcast(broadcastId: String, data: AnySerializable)
}
data class SystemBroadcastData(val broadcastId: String, val data: AnySerializable)
```

**Emit**: `BroadcastToSystemEventRunner` calls `get<SystemBroadcastChannel>().broadcast(...)`.
**Consume**: tiles with `trigger = OnSystemBroadcastEventTrigger(broadcastId)` execute.

| | ScreenTilesBroadcastChannel | SystemBroadcastChannel |
|---|---|---|
| Scope | Per-screen | App-global |
| Identity | `tileId: String?` | `broadcastId: String` |
| Who emits | `broadcastData()` in EventRunningScope | `BroadcastToSystemEventRunner` |
| Who consumes | TileRenderers via CompositionLocal | Tiles with `OnSystemBroadcast` trigger |

---

## 9. DataProcessor

Pluggable data transformation unit identified by string id.

```kotlin
interface DataProcessor {
    val id: String
    fun EventRunningScope.proccess(data: Any)
}
```

Built-in: `"EVENT_RUNNER"` — deserializes `data` as `List<EventSchema>` or `EventSchema` and executes inline.
Register custom processors in Koin: `bind<DataProcessor>()` with `named("MY_PROCESSOR")`.

---

## 10. NavigatorsHolder

Global registry of `NavigationController` instances.

```kotlin
fun registerNavigator(navigatorId: String, controller: NavigationController)
operator fun get(navigatorId: String): NavigationController?

// NavigationController:
fun navigate(destination: String, navigationData: Map<String, Any>, poppingUpTo: PoppingUpTo?)
fun navigateUp()
```

Used by `NavigateEventRunner` and `NavigateUpEventRunner`.

---

## 11. ScreenBehaviorsHolder

Screen-level state transitions. Accessed via `EventRunningScope.screenBehaviorsHolder`.

```kotlin
fun setState(state: ScreenBehaviorsHolder.State)
fun broadcastData(data: ScreenTilesBroadcastData)

sealed interface State {
    data class Success(val screenModel: ScreenModel) : State
    data object Failure : State
    data object Initial : State
}
```

---

## 12. TilesEditor

Tile manipulation API. Accessed via `EventRunningScope.tilesEditor`.

```kotlin
fun addTile(tileSchema: TileSchema, where: InsertionPosition = End)
fun addTile(tileSchema: TileSchema, groupingTileId: String, where: InsertionPosition = End)
fun addTiles(tileSchemas: List<TileSchema>, groupingTileId: String, where: InsertionPosition = End)
fun removeTile(tileId: String, groupingTileId: String? = null)
fun removeTiles(tileIds: List<String>, groupingTileId: String? = null)
fun replaceTiles(tileSchemas: List<TileSchema>, groupingTileId: String? = null)
fun wipeTiles(groupingTileId: String)
fun updateTile(tileId: String, updateData: Map<String, Any?>)
fun checkIfTileHasChildren(groupingTileId: String, childrenIds: List<String>): Boolean
fun getTileChildrenCount(groupingTileId: String): Int?
```

`InsertionPosition`: `Start`, `End`, `Before(tileId)`, `After(tileId)`.
All mutations trigger `updateState()` → Compose recomposition.

---

## 13. TilesOverlaysEditor

Overlay management. Accessed via `EventRunningScope.tilesOverlaysEditor`.

```kotlin
fun setBottomSheetTiles(tileSchemas: List<TileSchema>)
fun setDialogTiles(tileSchemas: List<TileSchema>)
```

---

## 13b. TilesValueProducer

Read-only tile value access. Accessed via `EventRunningScope.tilesValueProducer`.

```kotlin
fun getValueWithKey(tileId: String, key: String): Map<String, Any>?
```

Used by `GetData` with `DataSourceSchema.Tile(tileId, dataKey)`.
Key `"text"` on TextField tiles returns the current text value.

---

## 15. UpdateScope

Minimal context for `TileHolder.update()` / `EventHolder.update()`.

Update mechanism: encode current schema to JsonObject → encode updateData → merge (updateData keys override; style is deep-merged) → decode back to `T`.

---

## 16. MosaicSerializer

Central polymorphic serializer. Every new `TileSchema` or `EventSchema` must be registered here. Omitting causes silent runtime deserialization failure.

---

## 17. DataSourceSchema

| Variant | DataSource helper | Storage | Notes |
|---|---|---|---|
| `ApplicationPlainData` | `applicationPlainData()` | In-memory, app-scoped | Mutable; shared across all screens; lives until app closes |
| `ApplicationSegmentedData(id)` | `applicationSegmentedData("id")` | In-memory, app-scoped | Mutable; shared across all screens; lives until app closes |
| `ScreenPlainData` | `screenPlainData()` | In-memory, screen-scoped | Mutable |
| `ScreenSegmentedData(id)` | `screenSegmentedData("id")` | In-memory, screen-scoped | Mutable |
| `ScreenNavigationData` | `screenNavigationData()` | In-memory, screen-scoped | Read-only |
| `PlainDataBase` | `plainDataBase()` | SQLite persistent | Mutable |
| `SegmentedDataBase(id)` | `segmentedDataBase("id")` | SQLite persistent | Mutable |
| `Tile(tileId, dataKey)` | `tile("id", "key")` | Tile-exposed values | Read-only, GetData only |

---

## 18. AccessModeSchema

| Variant | Helper | Meaning |
|---|---|---|
| `Full` | `fullAccessMode()` | All records in namespace |
| `Single(dataId)` | `singleAccessMode("key")` | One record by ID |
| `Batch(dataIds, ...)` | `batchAccessMode(listOf(...))` | Multiple records; `allowMissingData`, `unwrapValuesToList` |

---

## Server DSL: CompositionLocal System

Inspired by Jetpack Compose. Passes values implicitly through the builder tree.

```kotlin
// Define
val LocalUser = compositionLocalOf<String> { error("Not provided") }

// Provide
CompositionLocalProvider(LocalUser provides "Rodrigo") {
    Column { SimpleText(text = LocalUser.current()) }
}
```

**Critical rule:** Call `snapshotLocals()` BEFORE passing a lambda to `addBuilder()`. Calling it inside the lambda reads locals after `CompositionLocalProvider` has already cleaned up — you get empty locals.

```kotlin
// CORRECT
val locals = snapshotLocals()
addBuilder(MyBuilder(template = { EventSchemaBuilderScope(locals).apply(eventTemplate).build() }))

// WRONG — locals will be empty inside the lambda
addBuilder(MyBuilder(template = { val locals = snapshotLocals(); /* already cleaned up */ }))
```
