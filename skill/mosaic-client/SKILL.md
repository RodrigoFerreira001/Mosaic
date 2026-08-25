---
name: mosaic-client
description: |
  Use when configuring Mosaic on the client side, or extending it with custom Tiles/Events —
  writing a TileRenderer, EventRunner, TileHolder, EventHolder, or Definition; registering them
  via MosaicDependencyInjectionConfig; or figuring out which client-side mechanisms (data holders,
  broadcasts, navigation, overlays) a custom TileRenderer/EventRunner can actually use. Trigger
  whenever the task involves mosaic-client code, bootstrapping MosaicApplication, or building a
  tile/event that isn't part of the built-in catalog.
---

# Mosaic Client — Setup & Extension Reference

This skill is self-contained: everything needed to bootstrap a client app and build a custom Tile/Event lives below, read directly from `mosaic-client`'s real source (`MosaicApplication.kt`, `EventRunningScope.kt`, `TileRenderingScope.kt`, `TileHolder.kt`, `EventHolder.kt`, `StyledWith.kt`). It's the client-side counterpart of the `mosaic-server` skill (which is about *writing screens with the DSL*, not extending the renderer/executor) and shares its conceptual foundation with the `mosaic` skill (architecture and runtime mechanisms) — load `mosaic` first if the "why" behind something here isn't obvious.

---

## 1. Bootstrapping a client app

```kotlin
MosaicApplication(
    applicationId = "MyApp",
    baseUrl = "https://api.example.com",
    themeConfig = mosaicThemeConfig(
        colorScheme = MosaicColorScheme(
            lightColorScheme = lightColorScheme(),
            darkColorScheme = darkColorScheme()
        )
    ),
    dependencyInjectionConfig = mosaicDependencyInjectionConfig(
        tileDefinitions = listOf(MyCustomTileDefinition),
        eventDefinitions = listOf(MyCustomEventDefinition),
        eventTriggerDefinition = listOf(MyCustomEventTriggerDefinition),
        additionalKoinModule = module { /* your own Koin bindings, if any */ },
        additionalSerializersModule = SerializersModule { /* only if a field type needs its own polymorphic serializer — independent of tile/event registration, see §2 */ },
        logger = MyMosaicLogger(),                                  // default: DefaultMosaicLogger()
        drawableResources = mapOf("logo" to Res.drawable.logo)      // names the `Image` tile can reference
    ),
    appSplash = { /* BoxScope — shown while the initial graph is loading */ }
)
```

This is a `@Composable` function — call it as your app's root content (typically inside `setContent { }`/the platform entry point).

---

## 2. Registering a custom Tile/Event — what you do and don't need to touch

You register a custom Tile or Event by passing its `Definition` object in `mosaicDependencyInjectionConfig(tileDefinitions = ..., eventDefinitions = ..., eventTriggerDefinition = ...)`, shown in §1. That is the **entire** registration step.

**You never construct or touch `MosaicSerializer` yourself.** Internally, `MosaicModules` builds it for you from exactly the definitions you passed:

```kotlin
// mosaic-client internal — you don't write this, shown so you know what happens with your definitions
MosaicSerializer(
    tileSerializers = tileDefinitions.associate { it.tileSchemaClass to it.tileSchemaClass.serializer() },
    eventSerializers = eventDefinitions.associate { it.eventSchemaClass to it.eventSchemaClass.serializer() },
    eventTriggerSerializers = eventTriggerDefinitions.associate { it.eventTriggerClass to it.eventTriggerClass.serializer() },
    additionalSerializersModule = additionalSerializersModule
)
```

`additionalSerializersModule` (a plain `kotlinx.serialization.modules.SerializersModule`) is a separate, optional knob — only needed if a field somewhere in your schema is itself a polymorphic type that needs its own serializer registration (independent of the Tile/Event schema classes, which are always handled automatically from `tileDefinitions`/`eventDefinitions`/`eventTriggerDefinition`). Most custom tiles/events never need it.

`TileRendererManager`, `TileHolderBuilderManager`, `EventRunnerManager`, and `EventHolderBuilderManager` are built the same way — each definition's `tileRenderer`/`tileHolderBuilder`/`eventRunner`/`eventHolderBuilder` gets wired to its schema's `KClass` automatically, merged with the framework's own built-in definitions. Nothing about the built-in vocabulary is privileged over what you register — you just don't have to assemble any of this machinery by hand.

---

## 3. The three-layer pattern, and where each piece lives

Every Tile and Event — built-in or custom — is split across three layers:

| Layer | Module | Kotlin shape | Role |
|---|---|---|---|
| Schema | `mosaic-core` | `data class`, `@Serializable` | The wire contract. |
| Builder | `mosaic-server` | `internal class` + public extension fun | The DSL function a backend author calls. Not your concern here unless you're also writing the server-side half. |
| Definition / Renderer (or Runner) / Holder / HolderBuilder | `mosaic-client` | `object`/`object`/`class`/`object` | Binds the schema's `KClass` to Compose rendering (tiles) or execution logic (events), plus a stateful `Holder` tracking the live tree. |

`Definition`, `Renderer`/`Runner`, and `HolderBuilder` are always `object` (stateless singletons). `Holder` is always a `class` (one live instance per tile/event in the tree).

---

## 4. Building a custom Tile — file-by-file, from real source

This walks through the actual shape of the built-in `Checkbox` tile (`mosaic-client/.../implementations/tile/tiles/inputs/checkbox/`) as the template — a tile with local state that also fires a remote trigger, which is the most complete case to learn from. Substitute `[Name]`/`[name]` with your own component name.

Every `TileSchema` carries the 5 base fields every built-in tile has — `id`, `events`, `style`, `searchableTerms`, `visibility` — plus whatever fields your tile needs. That schema lives in `mosaic-core` and is out of scope for this skill (see `mosaic-server` if you're also defining the DSL side).

### `[Name]TileDefinition.kt`

```kotlin
object CheckboxTileDefinition : TileDefinition<CheckboxTileSchema> {
    override val tileSchemaClass = CheckboxTileSchema::class
    override val tileRenderer = CheckboxTileRenderer
    override val tileHolderBuilder = CheckboxTileHolderBuilder
}
```

`TileDefinition<Schema : TileSchema>` requires exactly these 3 properties — nothing else.

### `[Name]TileHolder.kt`

```kotlin
class CheckboxTileHolder(
    override val id: String,
    override var tile: CheckboxTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<CheckboxTileSchema>() {

    override fun getTileSchema() = tile.copy(
        events = events.map { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is CheckboxTileEvents.OnCheckChanged -> {
                tile = tile.copy(checked = event.isChecked)
            }
        }
    }

    override fun produceValueWithKey(
        key: String
    ) = mapOf(key to tile.checked)
}
```

`TileHolder<T>` (`mosaic-client/.../foundation/tiles/holder/tile/TileHolder.kt`) is an `abstract class`, not an interface — key points:
- You override `getTileSchema(): T`, **not** a method called `get()` — `get()` is defined on the base class and calls your `getTileSchema()` only when the holder is dirty (`markAsDirty()` was called), caching the result otherwise.
- `tile` is `protected abstract var` on the base class — your subclass widens it to `override var tile: T` in the constructor, as shown.
- `events` is a non-nullable `MutableList<EventHolder<*>>`; `tiles` is a nullable `MutableList<TileHolder<*>>?` — leave `tiles` `null` for a leaf tile (no children), or provide a real mutable list for a container tile.
- `onTileEvent(event: TileEvent)` is an **open function with an unused-by-default body** (`= Unit`) that takes `TileEventScope` as an extension receiver — override it only if your tile has local, synchronously-visible state to mutate (see §9). Its own `TileEvent` type is a `sealed interface` you define yourself, one `data class`/`object` per distinct local mutation, e.g. `CheckboxTileEvents.OnCheckChanged(val isChecked: Boolean)`.
- `produceValueWithKey(key: String): Map<String, Any>?` — override this to expose a value for `GetData`/`EvaluateData` reading via the `Tile` data source (`tile(tileId, dataKey)` in the server DSL). Returns `null` (the default) if your tile has nothing to expose.
- For a container tile that needs `RadioButton`-style mutual exclusion, also override `handlesGroupEvent`/`onTileGroupEvent` (see §9).

### `[Name]TileHolderBuilder.kt`

```kotlin
object CheckboxTileHolderBuilder : TileHolderBuilder<CheckboxTileSchema, CheckboxTileHolder> {

    override fun BuilderScope.build(
        tileModel: CheckboxTileSchema
    ): CheckboxTileHolder = with(tileModel) {
        CheckboxTileHolder(
            id = id,
            tile = this,
            events = events.buildEventHolders()
            // container tile: also pass tiles = tiles.buildTileHolders()
        )
    }
}
```

`TileHolderBuilder<T, H>` requires one function: `fun BuilderScope.build(tileModel: T): H`. `BuilderScope` (injected as the extension receiver, not a parameter) exposes `buildEventHolders()`/`buildTileHolders()` as extension functions on `List<EventSchema>?`/`List<TileSchema>?` — call them directly on `events`/`tiles`, as shown; they return an empty mutable list when the source is `null`. `BuilderScope` also exposes `get<T>()`/`getOrNull<T>()` (Koin) and `decode<T>(data)`/`decodeOrNull<T>(data)` (via the framework's own `MosaicSerializer`) if your holder needs either at construction time.

### `[Name]TileRenderer.kt`

```kotlin
object CheckboxTileRenderer : TileRenderer<CheckboxTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: CheckboxTileSchema,
    ) {
        with(tileSchema) {
            Checkbox(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                enabled = enabled,
                checked = checked,
                onCheckedChange = { checked ->
                    triggerEvent(if (checked) EventTriggers.onCheck() else EventTriggers.onUncheck())
                    triggerEvent(EventTriggers.onCheckChanged())
                    dispatchEvent(CheckboxTileEvents.OnCheckChanged(checked))
                }
            )
        }
    }
}
```

`TileRenderer<T>` requires one `@Composable` function: `TileRenderingScope.Render(tileSchema: T)`. `Modifier.styledWith(style, onClick, onLongClick)` (`mosaic-client/.../ui/modifiers/StyledWith.kt`) is the real helper every built-in tile uses to apply a `StyleSchema` — it installs, in this fixed order, `windowInsets → margin → size(style.size) → clip → background → combinedClickable (only if onClick/onLongClick is non-null) → border → padding`. Always reach for it instead of hand-rolling the chain — it's what keeps every tile's spacing/click/border stacking behavior consistent. For a container tile, call `RenderChild(tileSchema)`/`RenderChildren(tileSchemas)` (both defined on `TileRenderingScope`, both `@Composable`) inside your layout composable instead of writing your own `Definition` lookup.

---

## 5. Building a custom Event — file-by-file, from real source

Modeled on the built-in `OpenExternalLink` event (synchronous, simple) and `GetData` (asynchronous, IO-bound) — between the two you have the shape for almost anything.

Every `EventSchema` carries `id`, `trigger`, `events` (its own children, each declaring which of *this* event's outgoing triggers runs it). That schema lives in `mosaic-core`, out of scope here.

### `[Name]EventDefinition.kt`

```kotlin
object OpenExternalLinkEventDefinition : EventDefinition<OpenExternalLinkEventSchema> {
    override val eventSchemaClass = OpenExternalLinkEventSchema::class
    override val eventRunner = OpenExternalLinkEventRunner
    override val eventHolderBuilder = OpenExternalLinkEventHolderBuilder
}
```

Same 3-property shape as `TileDefinition`.

### `[Name]EventHolder.kt`

`EventHolder<T>` (`mosaic-client/.../foundation/tiles/holder/event/EventHolder.kt`) mirrors `TileHolder`, adapted for events:

```kotlin
class OpenExternalLinkEventHolder(
    override val id: String,
    override var event: OpenExternalLinkEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<OpenExternalLinkEventSchema>() {

    override fun getEventSchema() = event.copy(
        events = events?.map { it.get() }
    )
}
```

Note the differences from `TileHolder`: you override `getEventSchema()` (not `get()`, not `getTileSchema()`); `events`/`tiles` are plain `List<...>?`, not `MutableList`. `EventHolder` is used **only for lookup/update by id** (`TriggerEvent`, `UpdateEvents`) — it never runs anything. Execution lives entirely in the `Runner` below.

### `[Name]EventHolderBuilder.kt`

```kotlin
object OpenExternalLinkEventHolderBuilder : EventHolderBuilder<OpenExternalLinkEventSchema, OpenExternalLinkEventHolder> {

    override fun BuilderScope.build(
        eventSchema: OpenExternalLinkEventSchema
    ) = with(eventSchema) {
        OpenExternalLinkEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.buildEventHolders()
        )
    }
}
```

### `[Name]EventRunner.kt` — synchronous / simple case

```kotlin
object OpenExternalLinkEventRunner : EventRunner<OpenExternalLinkEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: OpenExternalLinkEventSchema) {
        runSafely(
            onError = { throwable ->
                onTrigger(EventTriggers.onFailure(), data = throwable)
                logError(tag = "OpenExternalLinkEventRunner", throwable = throwable)
            }
        ) {
            openExternalLink(event.url)
            onTrigger(EventTriggers.onSuccess())
        }
    }
}
```

`EventRunner<T>` requires one suspend function: `EventRunningScope.runEvent(event: T)`. `runSafely(onError) { block }` (`dev.catbit.mosaic.core.extensions`) is a plain try/catch helper — `block` runs at most once, and any thrown exception routes to `onError` instead of propagating. This is the standard pattern for every built-in `EventRunner`: wrap the real work in `runSafely`, fire `onFailure` from `onError`.

### `[Name]EventRunner.kt` — asynchronous / IO-bound case

For work that should run off the main thread (network, database, file I/O), wrap the body in `withContext(Dispatchers.IO) { ... }` directly — there is no special "run on screen scope" wrapper to reach for:

```kotlin
object GetDataEventRunner : EventRunner<GetDataEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: GetDataEventSchema) {
        withContext(Dispatchers.IO) {
            onTrigger(EventTriggers.onStart())
            // ... read from data sources, using get<SomeUseCase>() for DI ...
            onTrigger(EventTriggers.onSuccess(), data = result)
        }
    }
}
```

Both `OpenExternalLinkEventDefinition` and `GetDataEventDefinition` land in `MosaicDependencyInjectionConfig.eventDefinitions`/`tileDefinitions` (§1) — omitting either causes a silent deserialization failure at runtime, not a compile error, since the schema's `KClass` never gets a matching `KSerializer` registered.

---

## 6. `EventRunningScope` — the full API a custom `EventRunner` receives

Real shape, from `mosaic-client/.../foundation/events/EventRunningScope.kt`:

```kotlin
data class EventRunningScope(
    val screenId: String,
    val triggerOwner: EventSchema,
    val incomingData: Any? = null,
    val tilesEditor: TilesEditor,
    val tilesEventDispatcher: TilesEventDispatcher,
    val tilesOverlaysEditor: TilesOverlaysEditor,
    val tilesValueProducer: TilesValueProducer,
    val screenDataHolder: ScreenDataHolder,
    val screenBehaviorsHolder: ScreenBehaviorsHolder,
) {
    suspend fun onTrigger(eventTrigger: EventTrigger, data: Any? = null)   // scans triggerOwner.events for a matching trigger, runs each match
    suspend fun runEventInline(eventSchema: EventSchema, data: Any? = null)   // invoke one EventSchema right now, bypassing trigger matching
    suspend fun runEventsInline(eventsSchema: List<EventSchema>, data: Any? = null)
    fun broadcastData(data: ScreenTilesBroadcastData)   // via screenBehaviorsHolder — the screen-scoped broadcast channel

    inline fun <reified T : Any> get(qualifier: Qualifier? = null, noinline parameters: ParametersDefinition? = null): T
    inline fun <reified T : Any> getOrNull(...): T?
    inline fun <reified T : Any> getAll(): List<T>

    fun logError(throwable: Throwable, tag: String)
    fun log(level: Level, msg: String)
}
```

`get<T>()`/`getOrNull<T>()`/`getAll<T>()` resolve from the same Koin scope every built-in `EventRunner` uses — this is how a custom runner reaches any of the mechanisms in §8 below, or any of your own `additionalKoinModule` bindings from §1.

---

## 7. `TileRenderingScope` — the full API a custom `TileRenderer` receives

Real shape, from `mosaic-client/.../foundation/tiles/renderer/TileRenderingScope.kt`:

```kotlin
data class TileRenderingScope(
    val tileId: String,
    val events: ImmutableList<EventSchema>?,
    val onEvent: (UIEvent) -> Unit
) {
    fun dispatchEvent(tileEvent: TileEvent)              // local, applied synchronously by this tile's own Holder.onTileEvent
    fun dispatchGroupEvent(tileGroupEvent: TileGroupEvent) // reaches every TileHolder in the tree whose handlesGroupEvent() returns true — see §9
    fun triggerEvent(trigger: EventTrigger, data: Any? = null)  // scans this tile's own `events` for a matching trigger, runs it

    @Composable fun RenderChild(tileSchema: TileSchema)
    @Composable fun RenderChildren(tileSchemas: ImmutableList<TileSchema>)
}
```

Being inside a `@Composable`, you can also `koinInject<T>()` any Koin single registered in the app — including the mechanisms in §8.

---

## 8. Client-side mechanisms a custom Tile/Event can reach

Each of these is a plain public Koin single (or interface) in `mosaic-client` — reach it with `get<T>()` from `EventRunningScope`/`BuilderScope`, or `koinInject<T>()` from a `@Composable` in `TileRenderingScope`. See the `mosaic` skill for what problem each one solves conceptually; this table is only about *how to reach it*:

| Mechanism | Type | How to reach it |
|---|---|---|
| `NetworkParametersHolder` | `class` | `get<NetworkParametersHolder>()` |
| `DataMailer` | `class` | `get<DataMailer>()` |
| `CancellableEventsHolder` | `class` | `get<CancellableEventsHolder>()` |
| `OverlayDisplayCallbackHolder` | `class` | `get<OverlayDisplayCallbackHolder>()` from `EventRunningScope`, `koinInject<OverlayDisplayCallbackHolder>()` from a `@Composable` |
| `ApplicationDataHolder` | `interface` | `get<ApplicationDataHolder>()` |
| `ScreenDataHolder` | `interface` | already `EventRunningScope.screenDataHolder` |
| `ScreenExtrasHolder` | `class` | `get<ScreenExtrasHolder>()` — relevant mainly if your custom tile hosts its own nested navigation graph, the way the built-in `NestedNavigationGraph` tile does |
| `NavigatorsHolder` | `class` | `get<NavigatorsHolder>()` |
| `SystemBroadcastChannel` | `class` | `get<SystemBroadcastChannel>()`; the screen-scoped one is already `EventRunningScope.broadcastData(...)` |
| `TilesManager` hierarchy | `class` | compose your own `MosaicScreen(parent = LocalTilesManager.current)` inside your `TileRenderer` for a tile that hosts its own mini backstack |
| overlay stacking (bottom sheets/dialogs) | via `TilesOverlaysEditor` | already `EventRunningScope.tilesOverlaysEditor` |
| `MosaicSerializer` | `class` | `get<MosaicSerializer>()` — you'll rarely need this directly; see §2 |
| `DrawableResourcesHolder` | `class` | `get<DrawableResourcesHolder>()` |
| `CameraManager` | `interface`, bound per-platform | `get<CameraManager>()` |
| `DataProcessor` | `interface`, Koin-multibound | not a single lookup — resolve every registered one via `getAll<DataProcessor>()` and match by `id` yourself, the same way `ProcessDataEventRunner` does; register your own by binding it (`single { MyProcessor } bind DataProcessor::class`) in `additionalKoinModule` (§1) |
| `Int.ThresholdReachedEffect(...)` | public extension fun (`mosaic-client/.../extensions/IntExtensions.kt`) | call directly — reuse it if building a custom lazy-list-style tile with pagination |
| `Platform` | plain `expect object`, not Koin-injected | reference `dev.catbit.mosaic.client.platform.Platform` directly — its fields are also what `MosaicHeadersPlugin` sends as `x-mosaic-*` headers on every outgoing request |

Client internals that are `internal` to the `mosaic-client` module (e.g. `MosaicColors`, `TemplateProcessor`) aren't reachable from outside it at all — Kotlin won't let a consuming app's module resolve the symbol, so there's nothing actionable to document about them here; they only affect behavior through the built-in events/tiles that already use them.

### `MosaicHeadersPlugin` — every outgoing request carries platform headers

`MosaicHeadersPlugin` (a Ktor client plugin, installed once in `MosaicModules.dataModule` onto the single `HttpClient` `MosaicNetworkImpl` shares) stamps **every** request the client sends — `SendNetworkRequest`, `UploadFile`, all 3 download events, `GetScreen`/`RefreshScreen`, the initial-graph fetch, the cache-version check — with 9 `x-mosaic-*` headers, read live off `Platform` on each request:

`x-mosaic-platform-name`, `x-mosaic-device`, `x-mosaic-os-version`, `x-mosaic-extra-info` (flattened `key=value` list, not JSON), `x-mosaic-screen-size`, `x-mosaic-screen-density`, `x-mosaic-locale`, `x-mosaic-timezone`, `x-mosaic-dark-mode`.

There is no per-request opt-out, no DSL flag to suppress this, and no way to intercept/rewrite it from a custom `EventRunner` — it's not part of the request pipeline any event or use case touches; it's a Ktor plugin sitting below all of them, on the one shared client. A custom `EventRunner` that issues its own HTTP calls outside `MosaicNetwork`/`MosaicRepository` (e.g. via its own injected `HttpClient`) does **not** get these headers for free — only requests that actually go through `MosaicNetwork` do. Read `Platform` directly (§8 above) if a custom event needs the same device info for its own purposes.

---

## 9. Implicit patterns worth replicating in a custom Tile/Event

These aren't APIs to call — they're conventions every built-in tile/event follows. A custom one should too, for consistency with the rest of the framework:

- **Trigger matching is the whole extension mechanism.** Your `TileRenderer` calls `dispatchEvent`/`triggerEvent`; your `EventRunner` calls `onTrigger(trigger, data)`. There is no listener registry anywhere — matching is always a plain equality check (`events.filter { it.trigger == eventTrigger }`) done fresh every time.
- **If your tile has local, instantly-visible state** (like `Checkbox`/`RadioButton`/`Tabs`), fire **both** a local `TileEvent` (via `dispatchEvent`, handled by your own `TileHolder.onTileEvent` override, applied synchronously) **and** the matching remote `EventTrigger` (via `triggerEvent`), on the same interaction — exactly as `CheckboxTileRenderer` does in §4. The local one always finishes first: `dispatchEvent` resolves synchronously on the UI thread and recomposes before returning; `triggerEvent` only schedules the matching events to run in the screen's coroutine scope.
- **If your tile needs a "mutual exclusion" group** (like `RadioButton`'s `groupId`), override `handlesGroupEvent(event: TileGroupEvent): Boolean` and `TileEventScope.onTileGroupEvent(event: TileGroupEvent)` on your `TileHolder` (both `open` on the base class, default no-op), and dispatch via `dispatchGroupEvent(...)` from the renderer. The group event reaches **every** tile in the whole screen's tree whose `handlesGroupEvent` returns true, and each one individually decides whether to react.
- **Follow the fixed style-application order** by always going through `Modifier.styledWith(style, onClick, onLongClick)` (§4) instead of composing your own chain — `windowInsets → margin → size → clip → background → click handling → border → padding`.
- **Declare your triggers with a `@Triggers([...])`-style annotation on your schema** even though nothing reads it at runtime in the built-in framework — it's the convention every built-in schema follows, and it's what a human (or another LLM) reading your schema's source will expect to find as living documentation.

---

## 10. See Also

- **`mosaic` skill** — the conceptual map: what each mechanism above is *for*, and how the whole request-to-pixels pipeline works.
- **`mosaic-server` skill** — if the task turns out to be about *using* the DSL to compose a screen, not extending the client.
