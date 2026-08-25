---
name: mosaic
description: |
  Use when the task is about understanding, discussing, or navigating the Mosaic SDUI framework
  itself — what it is, how mosaic-core/mosaic-server/mosaic-client fit together, how a Tile/Event
  goes from DSL call to rendered UI, or how a specific runtime mechanism (event chaining, trigger
  matching, data holders, broadcasts, navigation, overlays) actually works. Trigger for
  architecture questions, "how does X work under the hood", or when picking which of the other
  two Mosaic skills (mosaic-client, mosaic-server) actually applies to the task at hand. Not for
  writing DSL code (use mosaic-server) or extending the client (use mosaic-client) directly —
  this is the conceptual map that points to both.
---

# Mosaic — Architecture & Mechanisms

## 1. What Mosaic is

Mosaic is a **Server-Driven UI (SDUI)** framework for Kotlin Multiplatform. The backend describes a screen — its components, layout, styling, data bindings and interaction logic — as a serialized tree, and a Compose Multiplatform client deserializes and renders it live. There is no per-platform UI code to write for a new screen, and no client build to ship when a screen changes.

Three modules, one shared contract:

```
mosaic-core     — shared, @Serializable Schemas + the polymorphic serializer (MosaicSerializer).
                  No dependency on the other two modules — this is the wire contract both sides agree on.
    ↑
    ├── mosaic-server   — a type-safe Kotlin DSL that builds Schemas and serializes them to JSON.
    ├── mosaic-client   — Kotlin Multiplatform. Deserializes Schemas, renders them with Compose
    │                     Multiplatform, and executes Events.
    ├── sample-server   — a Ktor backend using mosaic-server; also the framework's interactive showcase.
    └── sample-client   — a reference client app consuming sample-server via mosaic-client.
```

Every displayable component is a **Tile** (`TileSchema`); every unit of behavior is an **Event** (`EventSchema`). Both follow the same three-layer split — **Schema** (`mosaic-core`, the wire contract), **Builder** (`mosaic-server`, the DSL function a backend developer writes), **Definition/Renderer/Holder/Runner** (`mosaic-client`, binds the schema to Compose rendering or execution logic).

Neither `mosaic-server` nor `mosaic-client` needs to know about the other's existence — both only depend on `mosaic-core`. That's what makes the contract type-safe end to end without codegen.

---

## 2. Which skill do you actually need?

- **Writing/composing a screen in the backend DSL** (`Screen{}`, `Button{}`, event chains, `Navigate`, etc.) → load `mosaic-server`.
- **Writing a custom `TileRenderer`/`EventRunner`, or bootstrapping `MosaicApplication`** → load `mosaic-client`.
- **Understanding how something works, deciding where a bug or a new mechanism belongs, or picking between the two skills above** → you're in the right place, keep reading.

---

## 3. The pipeline, end to end

```
mosaic-server:  Screen(id="login") { Column { Button(events = { SendNetworkRequest(...) }) } }
                    │  builders serialize to JSON via MosaicSerializer
                    ▼
mosaic-core:    JSON over HTTP — TileSchema/EventSchema tree, polymorphic by @SerialName
                    │  MosaicSerializer.decodeFromJsonElement (same class, same registry, client side)
                    ▼
mosaic-client:  TileHolderBuilderManager/EventHolderBuilderManager build a live TileHolder/EventHolder
                tree → TileRendererManager renders it via Compose → user taps something →
                EventManager runs the matching EventRunner → mutates the tree → recomposes
```

`TilesManager`, `EventManager` and `TileRendererManager` are created once per screen (`MosaicScreenStateHolder`), and every screen's tile tree is secretly rooted in a synthetic `ScreenTileSchema` (`id = "mosaic::root"`) that the DSL author never sees directly.

---

## 4. Explicit mechanisms — named classes you can point at

Each one exists to solve a specific problem — a named class/singleton you can point at and say "this is where it lives." The `mosaic-client` skill details, for each one, exactly how a custom `TileRenderer`/`EventRunner` reaches it (or doesn't).

| Mechanism | Solves |
|---|---|
| `NetworkParametersHolder` | Carrying a dynamically-computed value (a signed URL, a token) into the *next* network call, when the event issuing it can't know that value ahead of time. |
| `DataMailer` | Moving a value between two unrelated parts of the app (different screens) without navigation arguments. |
| `CancellableEventsHolder` | Long-running event chains (timers, polls) that need to be stoppable later, from elsewhere. |
| `ApplicationDataHolder` / `ScreenDataHolder` | Two different state lifetimes — survives navigation vs. dies with the screen. |
| `ScreenExtrasHolder` | Decoupling "the nav graph loaded" from "this screen was actually opened." |
| `NavigatorsHolder` | Letting any event, at any tree depth, trigger navigation without the backstack being threaded as a parameter. |
| `ScreenTilesBroadcastChannel` vs `SystemBroadcastChannel` | Pub/sub within one screen vs. app-wide. |
| Hierarchy of `TilesManager` / `LocalTilesManager` | Nested navigation graphs hosting their own tile tree inside a tile. |
| `stackableOverlays` | Bottom sheets/dialogs stacking, addressable by id, with a real exit animation. |
| `OverlayDisplayCallbackHolder` | Firing `OnDisplay` on `DisplayBottomSheet`/`DisplayModalBottomSheet`/`DisplayDialog` at the moment the overlay actually enters composition, not when it's merely registered (that's `OnSuccess`, earlier). |
| `MosaicSerializer` | Resolving polymorphic JSON to the right concrete type, extensible by third parties. |
| `DrawableResourcesHolder` | The `Image` tile resolving a name to an app-bundled asset. |
| `CameraManager` | Abstracting the device camera behind one platform-agnostic `takePicture()`, for `TakePicture`. Bound per-platform, not app-wide. |
| `DataProcessor` | Extension point for `ProcessData(processWith = id)` — a Koin-multibound interface (`id` + `process(data)`), resolved via `getAll<DataProcessor>()` and matched by `id`. Third parties add their own via `additionalKoinModule`. |
| `MosaicHeadersPlugin` | Stamps **every** outgoing request — `SendNetworkRequest`, `UploadFile`, all 3 downloads, `GetScreen`/`RefreshScreen`, the initial-graph fetch, the cache-version check, no exceptions, no opt-out — with 9 `x-mosaic-*` device/platform headers, read live from `Platform`. A backend author can always rely on these being present. |
| `MosaicColors` | Runtime-swappable theme, driven by `SetTheme`/`ResetTheme`. |
| Cache TTL + fallback | Staying usable offline without every load blocking on network. |
| `BuildContext` (server) | Propagating a DSL-authored ambient value through nested builders without explicit parameters. |
| `TemplateProcessor` | The `<\|path\|>` template engine behind `TransformData`/`UpdateTiles`. |
| `ThresholdReachedEffect` | Infinite-scroll pagination's "don't re-fire until the list grows" guard. |
| `GenericBuilder`/`GenericBuilderScope` (server) | Shared machinery every tile/event builder needs. |

---

## 5. Implicit mechanisms — conventions, not classes

No dedicated class enforces these — a typo or a missed step fails silently (it compiles, it serializes, it just never fires):

- **Event chaining / trigger matching** — a parent event fires a trigger value; the framework scans its own `events` list for children whose `trigger` matches *exactly* (`data class` equality, not a listener registry) and runs only those. This is the entire mechanism behind every multi-step flow in the DSL, and the primary extension point for custom code (`dispatchEvent`/`triggerEvent` on the tile side, `onTrigger` on the event side).
- **`incomingData` propagation** — each `onTrigger(trigger, data)` call passes `data` along; the matching child receives it as its own `incomingData`. No shared mutable context anywhere.
- **The `field ?: incomingData` fallback** — several event schemas accept either a literal value or fall back to `incomingData` when the field is omitted (`SendData.data`, `UploadFile.url` fed by staging).
- **Local `TileEvent` + remote `EventTrigger` on the same interaction** — every stateful input tile (`Checkbox`, `RadioButton`, `Tabs`, …) updates its own UI synchronously *and* fires a remote trigger, on the same tap. The local update always finishes first (`dispatchEvent` is synchronous, `triggerEvent` only schedules a coroutine).
- **`TileGroupEvent`** — `RadioButton`'s mutual-exclusion mechanism: a plain `groupId` field, no central group registry, every matching-type tile in the tree gets asked and decides for itself whether to react.
- **Two-phase overlay dismissal** — a bottom sheet/dialog flips `isDismissing` first, plays its exit animation, and only then is actually removed from the tree — because Compose recomposition is instant but the animation is a suspend call.
- **`GetScreen` → `ChangeScreenState`** — fetching a screen's content and installing it are deliberately two separate events, fused by default in `RefreshScreen` and in the `initialEvents` default of every `entry{}`.
- **Infinite-scroll pagination** — no dedicated event; four pieces wired by convention. `SendNetworkRequest` nested in a `LazyColumn`/`LazyRow`'s own `events`, `trigger = onScrollThresholdReached()`, fetches the next page; its response (an `EventList`, decoded via `ProcessData(processWith = "EVENT_RUNNER")`) swaps a loading placeholder for real content (`RemoveTiles`+`AddTiles`) *and* rewrites that same `SendNetworkRequest`'s own `url` for the next page (`UpdateEvents`) — skip the rewrite and every scroll re-fetches the same page forever. See `mosaic-server` skill for the full worked recipe. Layout gotcha: put `weightVertically(1f)` on a wrapping container, not on the lazy tile itself, or it never actually scrolls internally and `scrollThreshold` never re-fires.
- **`StyleSchema` application order** — a fixed sequence (`windowInsets → margin → size → clip → background → onClick → border → padding`) that determines how margin/padding/background/border interact visually.
- **`Modifier.size()` + `LocalXScope` availability** — `weight`/`span` sizing only works inside the matching container type; otherwise it's silently ignored.
- **`@Triggers` annotation** — source-only documentation (`AnnotationRetention.SOURCE`), visible to humans and tooling, never read at runtime.

---

## 6. Where to go next

- **`skill/mosaic-server`** — writing screens with the DSL: the complete, self-contained catalog of every tile and event (parameters, DSL snippets, triggers, behavior), plus imports, style utilities, event-chaining patterns, data flow, broadcasts, network requests, and reusable compositions.
- **`skill/mosaic-client`** — bootstrapping a client app, and extending it with custom tiles/events: full file-by-file templates, and exactly which of the mechanisms in §4/§5 above a third party can actually reach.

Both of those skills are self-contained — they don't require this one to be loaded, but this one is the fastest way to understand *why* something in either of them works the way it does.
