<p align="center">
  <img src="docs/assets/mosaic_logo.svg" alt="Mosaic" width="140" />
</p>

<p align="center"><b>Server-Driven UI for real Kotlin Multiplatform teams</b></p>
<p align="center">One backend describes the screen. One Compose Multiplatform client renders it — on Android, iOS, Desktop and Web.</p>

<p align="center">
  <a href="https://search.maven.org/search?q=g:dev.catbit+a:mosaic-core"><img src="https://img.shields.io/maven-central/v/dev.catbit/mosaic-core?label=Maven%20Central&color=2A6DB2" alt="Maven Central"></a>
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.x-7F52FF?logo=kotlin&logoColor=white">
  <img alt="Kotlin Multiplatform" src="https://img.shields.io/badge/Kotlin%20Multiplatform-Android%20|%20iOS%20|%20Desktop%20|%20Web-7F52FF?logo=kotlin&logoColor=white">
  <img alt="Compose Multiplatform" src="https://img.shields.io/badge/Compose%20Multiplatform-UI%20layer-4285F4?logo=jetpackcompose&logoColor=white">
  <a href="https://rodrigoferreira001.github.io/Mosaic/"><img src="https://img.shields.io/badge/docs-API%20reference-2A6DB2?logo=kotlin&logoColor=white" alt="API Docs"></a>
  <img alt="License" src="https://img.shields.io/badge/license-Apache%202.0-informational">
  <img alt="Status" src="https://img.shields.io/badge/status-in%20development-yellow">
</p>

<p align="center">
  <a href="https://rodrigoferreira001.github.io/Mosaic/showroom/">
    <img alt="Live Showcase" src="https://img.shields.io/badge/%E2%96%B6_Live_Showcase-Every_Tile_%26_Event%2C_running-2B6CA4?style=for-the-badge">
  </a>
  <br />
  <sub>sample-client (wasmJs) on GitHub Pages, talking to sample-server on Cloud Run — see "Deploying the showroom" below</sub>
</p>

---

Mosaic is a **Server-Driven UI framework**: the backend describes a screen — its components, layout, styling, data bindings and interaction logic — as a serialized tree, and the client (Android, iOS, Desktop, Web) deserializes and renders it live with Compose Multiplatform. There is no per-platform UI code to write for a new screen, and no client build to ship when a screen changes.

It's also, deliberately, a **real KMP project on both sides of the wire**: the DSL your backend team writes (`mosaic-server`) and the schemas it produces (`mosaic-core`) are the same Kotlin types the client (`mosaic-client`) deserializes and renders — no OpenAPI codegen, no hand-written parallel models, no drift between what the server means and what the client understands. If you already have Kotlin on the backend, Mosaic gives you a genuinely shared contract instead of a JSON convention everyone promises to keep in sync.

This is a personal project, built and maintained by one person, and it's under active development. Some subsystems (persisted data, offline caching, a few event/tile combinations) are still being reworked; everything documented below is implemented and runnable, not aspirational. The current focus and open items are tracked in [`ROADMAD.md`](ROADMAD.md).

## Why this exists

A native client and a JSON-over-HTTP backend agree on a fixed UI contract at build time. Changing that contract — adding a field to a form, reordering a screen, changing what a button does on success versus failure — means shipping new client code, through app store review, on every platform you support. If you support Android, iOS, desktop and web from one team, you're maintaining the same screen logic four times.

Server-Driven UI moves that contract from build time to request time. The client stops hardcoding screens and becomes a generic interpreter for a fixed vocabulary of components (**Tiles**) and behaviors (**Events**). The backend team owns screen composition, conditional logic, network orchestration and navigation — in type-safe Kotlin — and ships changes without touching the client binary.

Mosaic is a full implementation of that idea: a shared type-safe contract (`mosaic-core`), a backend DSL that builds that contract (`mosaic-server`), and a rendering/execution engine that consumes it (`mosaic-client`).

## Setup

All three modules are published to Maven Central under `dev.catbit`. `mosaic-core` holds the shared Schemas that both the server DSL and the client renderer are built on, and it's declared as an `implementation` (not `api`) dependency on both sides — add it explicitly alongside whichever module you're pulling in.

### Server setup

**`gradle/libs.versions.toml`**
```toml
[versions]
mosaic = "1.1.0"

[libraries]
mosaic-core = { module = "dev.catbit:mosaic-core", version.ref = "mosaic" }
mosaic-server = { module = "dev.catbit:mosaic-server", version.ref = "mosaic" }
```

**`build.gradle.kts`**
```kotlin
dependencies {
    implementation(libs.mosaic.core)
    implementation(libs.mosaic.server)
}
```

Or with direct coordinates:

```kotlin
dependencies {
    implementation("dev.catbit:mosaic-core:1.1.0")
    implementation("dev.catbit:mosaic-server:1.1.0")
}
```

That's the whole backend footprint — `mosaic-server` is a plain JVM library, no Ktor server required by the DSL itself (`sample-server` just happens to use Ktor to expose it over HTTP).

### Client setup

**`gradle/libs.versions.toml`**
```toml
[versions]
mosaic = "1.1.0"

[libraries]
mosaic-core = { module = "dev.catbit:mosaic-core", version.ref = "mosaic" }
mosaic-client = { module = "dev.catbit:mosaic-client", version.ref = "mosaic" }
```

**`build.gradle.kts`** (inside a Compose Multiplatform module's `commonMain` source set)
```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.mosaic.core)
            implementation(libs.mosaic.client)
        }
    }
}
```

Or with direct coordinates:

```kotlin
commonMain.dependencies {
    implementation("dev.catbit:mosaic-core:1.1.0")
    implementation("dev.catbit:mosaic-client:1.1.0")
}
```

`mosaic-client` targets Android, iOS (`iosArm64`/`iosSimulatorArm64`), Desktop (`jvm`) and Web (`wasmJs`) — pick whichever of those your Compose Multiplatform module already targets, nothing extra to configure per-platform. `mavenCentral()` needs to be in your `dependencyResolutionManagement`/`repositories` block, same as any other Central-hosted dependency.

## A screen, in the DSL

```kotlin
// mosaic-server
Screen(id = "login") {
    Column {
        TextField(id = "email", label = "Email")
        TextField(id = "password", label = "Password", visualTransformation = keyboardVisualTransformationPassword())

        Button(
            id = "submit",
            text = "Sign in",
            events = {
                SendNetworkRequest(
                    trigger = EventTriggers.onClick(),
                    url = "/api/login",
                    method = HttpMethod.POST,
                    events = {
                        UpdateData(
                            trigger = EventTriggers.onSuccess(),
                            updates = {
                                update(
                                    dataSource = applicationSegmentedData("auth"),
                                    updateData = explicitIncomingUpdateData(dataId = "session")
                                )
                            },
                            events = {
                                Navigate(trigger = EventTriggers.onSuccess(), destination = "home", navigatorId = "root")
                            }
                        )
                        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "Login failed")
                    }
                )
            }
        )
    }
}
```

Nothing in that tree references platform code. The client that renders it doesn't know "login" exists ahead of time — it knows how to render a `Column`, a `TextField`, a `Button`, and how to run `SendNetworkRequest` → `UpdateData` → `Navigate`/`DisplaySnackbar` as a chained event graph, because those are part of the fixed vocabulary shipped in `mosaic-core`/`mosaic-client`. Event chaining is driven entirely by trigger matching (`onSuccess()`, `onFailure()`, ...) on each node's `events` field — there's no callback wiring to write on the client side, and no code generation step in between.

Bootstrapping a client is a single composable:

```kotlin
// mosaic-client (Compose entry point)
MosaicApplication(
    applicationId = "MyApp",
    baseUrl = "https://api.example.com",
    appSplash = { Text("Loading…") }
)
```

## What can I display with Mosaic?

**48 typed Tile schemas**, covering everything a Material 3 screen typically needs. Every tile — regardless of category — shares a base contract: `id`, `events`, `style` (size/margin/padding/background/border/clip/window insets), `visibility` (`VISIBLE`/`INVISIBLE`/`GONE`), and `searchableTerms` (used by parent containers to filter children).

**Text**

| Tile | What it does |
|---|---|
| `SimpleText` | Styled text: typography, color, weight, decoration, letter spacing, auto-sizing, line clamping. |

**Buttons**

| Tile | What it does |
|---|---|
| `Button` | Material 3 button (filled, elevated, filled-tonal, outlined or text), optional icon, built-in loading state. |
| `IconButton` | Icon-only tappable button (default/filled/filled-tonal/outlined). |
| `FloatingActionButton` | Standard/medium/large FAB. |

**Chips**

| Tile | What it does |
|---|---|
| `AssistChip` | Suggests a single contextual action. |
| `FilterChip` | Toggleable chip used to filter a list or result set — selection state is tracked client-side. |
| `InputChip` | Represents a piece of user input (e.g. a selected tag). |
| `SuggestionChip` | Surfaces a dynamically generated suggestion. |

**Inputs**

| Tile | What it does |
|---|---|
| `TextField` | Single/multi-line text input — label, keyboard options, visual transformation (e.g. password/custom mask), validation state. |
| `Checkbox` | Boolean checkbox. |
| `Switch` | Boolean toggle. |
| `RadioButton` | Single-select control — mutual exclusion within a `groupId` is resolved entirely client-side, no round-trip needed. |
| `DropdownList` | Exposed dropdown / select-one menu. |
| `DatePicker` | Material 3 date picker field. |
| `TimePicker` | Material 3 time picker field. |

**Images & icons**

| Tile | What it does |
|---|---|
| `Icon` | Vector icon — Material Symbols. |
| `Image` | Static, locally bundled image (resolved from a name the client registers, not a URL). |
| `AsyncImage` | Remotely loaded image (Coil 3) — URL, raw bytes or base64. |

**Containers & layout**

| Tile | What it does |
|---|---|
| `Column` | Vertical layout container, optionally scrollable. |
| `LazyColumn` | Virtualized, scrollable vertical list — supports scroll-threshold pagination. |
| `Row` | Horizontal layout container, optionally scrollable. |
| `LazyRow` | Virtualized, scrollable horizontal list. |
| `Box` | Stacked/overlapping layout container. |
| `Card` | Material 3 card surface (default/elevated/outlined). |
| `Grid` | CSS-grid-like layout with explicit column/row tracks. |
| `FlexBox` | Flexbox-style wrapping container. |
| `FlowRow` | Row that wraps children onto multiple lines when it runs out of space. |
| `Pager` | Swipeable, paged container. |
| `Carousel` | Material 3 carousel container — shares its scroll-control channel with `Pager`. |
| `Shimmer` | Skeleton/shimmer placeholder wrapper for loading states. |
| `PullToRefresh` | Pull-to-refresh gesture wrapper, paired with the `StopRefreshing` event. |
| `AdaptiveVisibility` | Shows/hides its children based on window width and height breakpoints. |
| `LazyTiles` | Self-loading container that fetches its own children from a remote endpoint, independent of the event pipeline. |
| `SelectionContainer` | Enables native text selection over its children. |

**App bars**

| Tile | What it does |
|---|---|
| `TopAppBar` | Screen top bar (default/center-aligned/medium/large) — title, navigation icon, actions. |
| `BottomAppBar` | Screen bottom bar with actions and an optional FAB. |

**Navigation**

| Tile | What it does |
|---|---|
| `Tabs` | Primary/secondary tab row with content switching. |
| `NavigationBar` | Bottom navigation bar. |
| `NavigationRail` | Side navigation rail for larger screens. |
| `NestedNavigationGraph` | Embeds an independent navigation graph and back stack inside a screen. |

**Progress**

| Tile | What it does |
|---|---|
| `CircularProgressIndicator` | Circular, determinate or indeterminate progress. |
| `LinearProgressIndicator` | Linear, determinate or indeterminate progress. |

**Feedback & overlays**

| Tile | What it does |
|---|---|
| `Badge` | Small status/count indicator — you position it yourself, it's not an auto-anchored `BadgedBox`. |
| `SearchBar` | Search input with a leading/trailing slot (any tile, not just an icon) and a built-in clear affordance. |
| `Menu` | Dropdown menu, opened and closed via the `ToggleMenu` event. |
| `Popup` | Anchored popup surface with configurable alignment/offset, opened and closed via the `TogglePopup` event. |
| `Tooltip` | Platform-gesture-driven tooltip — the server never opens or observes it. |

**System**

| Tile | What it does |
|---|---|
| `SystemBroadcastListener` | Transparent wrapper that renders its children unchanged while listening for system-wide broadcasts. |

## What can I do with Mosaic?

**67 typed Event schemas**, keyed by **72 `EventTrigger` types** (`OnClick`, `OnSuccess`, `OnFailure`, `OnStart`, plus scroll, keyboard, lifecycle, permission and network-status triggers). Every event shares `id`, `trigger` (what fires it) and `events` (children dispatched by trigger once this event completes) — which is what makes chaining work: `SendNetworkRequest` → `onSuccess()` → `UpdateData` → `onSuccess()` → `Navigate` is one declarative tree, no glue code.

**Tile management**

| Event | What it does |
|---|---|
| `AddTiles` | Inserts one or more tiles into a container — start, end, index, or before/after a given tile ID. |
| `RemoveTiles` | Removes tiles by ID from a container. |
| `UpdateTiles` | Partially updates tile fields via JSON merge, without altering tile type or tree structure. |
| `ReplaceTiles` | Replaces all children of a container with a new list. |
| `WipeTiles` | Removes all children from a container. |
| `ReloadLazyTiles` | Forces a `LazyTiles` tile to re-fetch its content. |
| `CheckIfTileContainsChildren` | Checks whether a container currently holds children with every specified ID. |
| `GetTileChildrenCount` | Returns the number of direct children of a container tile. |

**Event orchestration**

| Event | What it does |
|---|---|
| `RunEvents` | Fires its own `events` list as a payload, all of them, unconditionally — the "fan-out" primitive. |
| `RunCancellableEvents` | Same, but wrapped in a coroutine job registered under a `cancellableEventId` so it can be cancelled mid-flight. |
| `CancelEvents` | Cancels a job previously registered by `RunCancellableEvents`. |
| `TriggerEvent` | Looks up another event already declared in the tree by ID and runs it inline. |
| `UpdateEvents` | Partially updates other events' fields via JSON merge, by ID. |

**Navigation**

| Event | What it does |
|---|---|
| `Navigate` | Pushes a destination onto a named navigator's back stack, with optional `popUpTo`. |
| `NavigateUp` | Pops the back stack by one entry. |
| `NavigateClearingStack` | Clears the back stack and pushes a single destination. |

**Screen**

| Event | What it does |
|---|---|
| `GetScreen` | Fetches the current screen's content from the server — doesn't apply it; pair with `ChangeScreenState`. |
| `RefreshScreen` | Fetches and applies the current screen in one step (`GetScreen` + `ChangeScreenState` fused). |
| `ChangeScreenState` | Transitions the screen to `Initial`/`Success`/`Failure` directly, with no network call. |

**Data**

| Event | What it does |
|---|---|
| `SendData` | Stores a value in the in-process `DataMailer`, single-consumption, for cross-screen retrieval. |
| `CheckForReceivedData` | Reads a value from `DataMailer` by key. |
| `GetData` | Reads from one or more data sources (in-memory or persisted, plain or segmented, tile value, navigation data) with `Full`/`Single`/`Batch` access. |
| `UpdateData` | Writes to a data source, including explicit `null` on in-memory sources. |
| `RemoveData` | Deletes data from a source — single key, batch, or the entire store. |
| `ProcessData` | Hands `incomingData` to a registered `DataProcessor`, resolved by ID. |
| `TransformData` | Reshapes `incomingData` through a template with `<\|path\|>`/`</path/>` placeholders. |
| `EvaluateData` | Evaluates a boolean expression tree against available data and branches `onSuccess`/`onFailure` — the mechanism for server-authored conditional logic. |

**Networking**

| Event | What it does |
|---|---|
| `SendNetworkRequest` | Sends an HTTP request with a composed body, headers, query parameters and timeout. |
| `UploadFile` | Uploads the incoming `PlatformFile` as a raw binary body — built for the signed-URL upload pattern (GCS/S3). |
| `DownloadFile` | Downloads to the platform's public Downloads location, with progress reporting. |
| `DownloadFileToDisk` | Downloads to the app's private sandbox storage. |
| `DownloadFileToMemory` | Downloads straight into memory as a `ByteArray`. |
| `SetIncomingDataToNetworkParamsHolderBody` | Stages `incomingData` as the body of the next network event in the chain. |
| `SetIncomingDataToNetworkParamsHolderHeaders` | Stages `incomingData` as headers, merged into the next network event. |
| `SetIncomingDataToNetworkParamsHolderQueryParameters` | Stages `incomingData` as query parameters for the next network event. |
| `SetIncomingDataToNetworkParamsHolderUrl` | Stages `incomingData` as the URL of the next network event. |

**File system**

| Event | What it does |
|---|---|
| `SaveFile` | Saves `incomingData` (as `ByteArray`) to the file system. |
| `GetFile` | Reads a file back — as bytes, a stream, a `PlatformFile`, a decoded map, or base64. |
| `DeleteFile` | Deletes a file by name. |
| `OpenFilePicker` | Opens the system file picker; delivers the selection to child events. |
| `TakePicture` | Opens the device camera via Mosaic's own per-platform `CameraManager`, with compression/resize options. |
| `GetImageFromGallery` | Opens the device gallery pre-filtered to images, same compression/resize contract as `TakePicture`. |

**Overlays**

| Event | What it does |
|---|---|
| `DisplayDialog` / `DismissDialog` | Shows/dismisses a dialog with a given tile tree, identified by `dialogId`. |
| `DisplayModalBottomSheet` / `DismissModalBottomSheet` | Shows/dismisses a modal bottom sheet (own scrim), identified by `modalBottomSheetId`. |
| `DisplayBottomSheet` / `DismissBottomSheet` | Shows/dismisses a non-modal bottom sheet — screen behind stays interactive. |
| `DisplayNavigationDrawer` / `DismissNavigationDrawer` | Opens/closes the screen's navigation drawer (fire-and-forget broadcast, no ID needed — one per screen). |
| `DisplaySnackbar` / `DismissSnackbar` | Shows a snackbar (message, duration, optional action) / dismisses it programmatically. |

**Menu & popup**

| Event | What it does |
|---|---|
| `ToggleMenu` | Toggles the expanded state of a `Menu` tile, by ID. |
| `TogglePopup` | Toggles the expanded state of a `Popup` tile, by ID. |

**Time & scroll**

| Event | What it does |
|---|---|
| `StartCountdownTimer` | Starts a countdown, firing a tick trigger per step and a finish trigger at zero. |
| `StartTimeLoop` | Fires a trigger on a fixed interval, indefinitely, until cancelled via `RunCancellableEvents`. |
| `ScrollColumnTile` / `ScrollRowTile` / `ScrollPagerTile` | Programmatically scrolls a Column/Row/Pager (or Carousel) tile, by ID. |
| `StopRefreshing` | Stops the pull-to-refresh spinner on a `PullToRefresh` tile. |

**Security & system**

| Event | What it does |
|---|---|
| `RequestPermission` | Requests one or more runtime permissions using each platform's native mechanism. |
| `BroadcastToSystem` | Sends a named, app-wide broadcast for cross-screen or host-app communication. |
| `CheckIfHasInternetConnection` | Checks whether the device currently has an active internet connection. |
| `OpenExternalLink` | Opens a URL in the platform's external browser/handler. |
| `DropCaches` | Clears the local screen/graph/version cache, selectively or entirely. |

**Theme**

| Event | What it does |
|---|---|
| `SetTheme` | Overrides the app's Material 3 color scheme (light + dark) globally at runtime. |
| `ResetTheme` | Reverts a `SetTheme` override, restoring the app's default color scheme. |

## Data layer

Two storage tiers, each with plain (flat key-value) and segmented (bucketed) variants:

- **In-memory**, scoped to either the current screen or the whole application session.
- **Persistent**, backed by a local database, surviving app restarts, with TTL-based caching for screens and the navigation graph itself.

`GetData`, `UpdateData` and `RemoveData` read and write both tiers with `Full`/`Single`/`Batch` access modes, plus two extra sources that are read-only or view-only: `Tile` (reads the live value a specific tile currently holds, e.g. a `TextField`'s text) and `ScreenNavigationData` (the arguments the current screen was navigated in with).

## Extensibility

Consumers of `mosaic-client` register their own Tiles and Events without forking the framework:

```kotlin
MosaicApplication(
    applicationId = "MyApp",
    baseUrl = "https://api.example.com",
    dependencyInjectionConfig = mosaicDependencyInjectionConfig(
        tileDefinitions = listOf(MyCustomTileDefinition),
        eventDefinitions = listOf(MyCustomEventDefinition),
        additionalSerializersModule = myCustomSerializersModule
    ),
    appSplash = { }
)
```

Custom schemas plug into the same `MosaicSerializer` polymorphic registry and the same `Definition` → `Renderer`/`Runner` lookup as everything shipped in `mosaic-core`, and get access to the same set of client mechanisms (data holders, broadcasts, navigation, overlay stack) that built-in tiles/events use — nothing about the framework's own vocabulary is privileged.

## Architecture

Every Tile and Event follows the same three-layer split:

| Layer | Module | What it is |
|---|---|---|
| Schema | `mosaic-core` | `@Serializable` data class — the wire contract. Shared source of truth for server and client. |
| Builder | `mosaic-server` | Type-safe DSL function that constructs a Schema — what a backend developer writes. |
| Definition / Renderer / Holder / Runner | `mosaic-client` | Binds a Schema class to a Compose renderer (Tiles) or execution logic (Events), plus a stateful Holder that tracks the live tree. |

```
mosaic-core   — shared Schemas, MosaicSerializer (polymorphic JSON)
    ↑
    ├── mosaic-server   — Kotlin DSL, builds and serializes screens
    ├── mosaic-client   — deserializes, renders (Compose Multiplatform), runs events
    ├── sample-server   — reference Ktor backend, doubles as the interactive showcase
    └── sample-client   — reference Android/Desktop client
```

For the full internal architecture — how tiles/events are built and executed, and the explicit and implicit mechanisms that hold the runtime together — see [`.claude/context/architecture.md`](.claude/context/architecture.md). The complete field-by-field reference for every Tile and Event lives in [`.claude/context/tiles-catalog.md`](.claude/context/tiles-catalog.md) and [`.claude/context/events-catalog.md`](.claude/context/events-catalog.md).

## Tech stack

| Concern | Library |
|---|---|
| Language | Kotlin 2.x, Kotlin Multiplatform |
| UI | Compose Multiplatform — Android, iOS, Desktop (JVM), Web (Wasm) |
| Serialization | kotlinx.serialization, fully polymorphic schema registry |
| Networking | Ktor Client / Ktor Server |
| DI | Koin |
| Concurrency | Kotlin Coroutines |
| Local storage | Room (multiplatform) |
| Images | Coil 3 |
| Navigation | Navigation 3 (adaptive layouts, ViewModel-integrated) |

## Running the samples

```bash
# Ktor backend, serves sample screens on :9090 — this is what powers the showcase
./gradlew sample-server:run

# Desktop client
./gradlew sample-client:run

# Everything
./gradlew build
```

## Deploying the showroom

The live showcase is `sample-client`'s wasmJs build on GitHub Pages, talking to `sample-server` on Cloud Run behind Firebase Hosting. Both deploy automatically from `.github/workflows/` on every push to `main` that touches the relevant modules — `docs.yml` also builds and publishes the wasmJs app under `/showroom/`, and `deploy-server.yml` builds `sample-server/Dockerfile` and deploys it.

The server-side workflow needs a one-time manual setup in the target GCP/Firebase project (`mosaicsampleserver`), since this can't be provisioned from a PR:

1. Enable the Cloud Run, Cloud Build, Artifact Registry and Firebase Hosting APIs on the project.
2. Create a service account with the **Cloud Run Admin**, **Cloud Build Editor**, **Artifact Registry Writer**, **Service Account User** and **Firebase Hosting Admin** roles.
3. Generate a JSON key for that service account and add it as the `GCP_SA_KEY` secret in this repo's GitHub Actions settings.

Once that's in place, pushing to `main` is enough — no manual `gcloud`/`firebase` commands needed afterward.

## License

[Apache License 2.0](LICENSE).
