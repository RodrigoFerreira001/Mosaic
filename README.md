<p align="center">
  <img src="docs/assets/mosaic_logo.svg" alt="Mosaic" width="140" />
</p>

<p align="center">Server-Driven UI framework for Kotlin Multiplatform</p>

<p align="center">
  <a href="https://search.maven.org/search?q=g:dev.catbit+a:mosaic-core"><img src="https://img.shields.io/maven-central/v/dev.catbit/mosaic-core?label=Maven%20Central&color=2A6DB2" alt="Maven Central"></a>
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.x-7F52FF?logo=kotlin&logoColor=white">
  <img alt="Kotlin Multiplatform" src="https://img.shields.io/badge/Kotlin%20Multiplatform-Android%20|%20iOS%20|%20Desktop%20|%20Web-7F52FF?logo=kotlin&logoColor=white">
  <img alt="Compose Multiplatform" src="https://img.shields.io/badge/Compose%20Multiplatform-UI%20layer-4285F4?logo=jetpackcompose&logoColor=white">
  <img alt="License" src="https://img.shields.io/badge/license-Apache%202.0-informational">
  <img alt="Status" src="https://img.shields.io/badge/status-in%20development-yellow">
</p>

---

Mosaic is a Server-Driven UI framework: the backend describes a screen — its components, layout, styling, data bindings and interaction logic — as a serialized tree, and the client (Android, iOS, Desktop, Web) deserializes and renders it live with Compose Multiplatform. There is no per-platform UI code to write for a new screen and no client build to ship when a screen changes.

This is a personal project, built and maintained by one person, and it is under active development. Some subsystems (persisted data, offline caching, a few event/tile combinations) are still being reworked; the architecture and everything documented below is implemented and runnable, not aspirational. The current focus and open items are tracked in [`ROADMAD.md`](ROADMAD.md).

## Why this exists

A native client and a JSON-over-HTTP backend agree on a fixed UI contract at build time. Changing that contract — adding a field to a form, reordering a screen, changing what a button does on success versus failure — means shipping new client code, through app store review, on every platform you support. If you support Android, iOS, desktop and web from one team, you're maintaining the same screen logic four times.

Server-Driven UI moves that contract from build time to request time. The client stops hardcoding screens and becomes a generic interpreter for a fixed vocabulary of components (Tiles) and behaviors (Events). The backend team owns screen composition, conditional logic, network orchestration and navigation, in Kotlin, and ships changes without touching the client binary.

Mosaic is a full implementation of that idea: a shared type-safe contract (`mosaic-core`), a backend DSL to build that contract (`mosaic-server`), and a rendering/execution engine that consumes it (`mosaic-client`).

## Setup

All three modules are published to Maven Central under `dev.catbit`. `mosaic-core` holds the shared Schemas that both the server DSL and the client renderer are built on, and it's declared as an `implementation` (not `api`) dependency on both sides — add it explicitly alongside whichever module you're pulling in.

### Server setup

**`gradle/libs.versions.toml`**
```toml
[versions]
mosaic = "1.0.0"

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
    implementation("dev.catbit:mosaic-core:1.0.0")
    implementation("dev.catbit:mosaic-server:1.0.0")
}
```

That's the whole backend footprint — `mosaic-server` is a plain JVM library, no Ktor server required by the DSL itself (`sample-server` just happens to use Ktor to expose it over HTTP).

### Client setup

**`gradle/libs.versions.toml`**
```toml
[versions]
mosaic = "1.0.0"

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
    implementation("dev.catbit:mosaic-core:1.0.0")
    implementation("dev.catbit:mosaic-client:1.0.0")
}
```

`mosaic-client` targets Android, iOS (`iosArm64`/`iosSimulatorArm64`), Desktop (`jvm`) and Web (`wasmJs`) — pick whichever of those your Compose Multiplatform module already targets, nothing extra to configure per-platform. `mavenCentral()` needs to be in your `dependencyResolutionManagement`/`repositories` block, same as any other Central-hosted dependency.

## A screen, in the DSL

```kotlin
// mosaic-server
Screen(id = "login") {
    Column {
        TextField(id = "email", label = "Email")
        TextField(id = "password", label = "Password", visualTransformation = passwordTransformation())

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

Nothing in that tree references platform code. The client that renders it doesn't know "login" exists ahead of time — it knows how to render a `Column`, a `TextField`, a `Button`, and how to run `SendNetworkRequest` → `UpdateData` → `Navigate`/`DisplaySnackbar` as a chained event graph, because those are part of the fixed vocabulary shipped in `mosaic-core`/`mosaic-client`. Event chaining is driven entirely by trigger matching (`onSuccess()`, `onFailure()`, ...) on each node's `events` field — there's no callback wiring on the client side to write.

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

46 typed Tile schemas, covering everything a Material 3 screen typically needs. Every tile — regardless of category — shares a base contract: `id`, `events`, `style` (size/margin/padding/background/border/clip/window insets), `visibility` (`VISIBLE`/`INVISIBLE`/`GONE`), and `searchableTerms` (used by parent containers to filter children).

**Text**

| Tile | What it does |
|---|---|
| `SimpleText` | Styled text: typography, weight, decoration, letter spacing, line clamping, and step-based auto-sizing. |

**Buttons**

| Tile | What it does |
|---|---|
| `Button` | Material 3 button (filled, outlined, text or elevated) with an optional leading icon. |
| `IconButton` | Icon-only tappable button. |
| `FloatingActionButton` | Standard or extended FAB. |

**Chips**

| Tile | What it does |
|---|---|
| `AssistChip` | Suggests a single contextual action. |
| `FilterChip` | Toggleable chip used to filter a list or result set. |
| `InputChip` | Represents a piece of user input (e.g. a selected tag), with an optional remove affordance. |
| `SuggestionChip` | Surfaces a dynamically generated suggestion. |

**Inputs**

| Tile | What it does |
|---|---|
| `TextField` | Single/multi-line text input — label, keyboard options, visual transformation (e.g. password masking), validation state. |
| `Checkbox` | Boolean/tri-state checkbox. |
| `RadioButton` | Single-select radio control. |
| `Switch` | Boolean toggle. |
| `DropdownList` | Exposed dropdown / select-one menu. |
| `DatePicker` | Material 3 date picker field. |
| `TimePicker` | Material 3 time picker field. |

**Images & icons**

| Tile | What it does |
|---|---|
| `Icon` | Vector icon — Material Symbols or a custom drawable resource. |
| `Image` | Static, locally bundled image. |
| `AsyncImage` | Remotely loaded image (Coil 3) with placeholder/error states. |

**Containers & layout**

| Tile | What it does |
|---|---|
| `Column` | Vertical layout container. |
| `LazyColumn` | Virtualized, scrollable vertical list. |
| `Row` | Horizontal layout container. |
| `LazyRow` | Virtualized, scrollable horizontal list. |
| `Box` | Stacked/overlapping layout container. |
| `Card` | Material 3 card surface. |
| `Grid` | Grid layout container. |
| `FlexBox` | Flexbox-style wrapping container. |
| `FlowRow` | Row that wraps children onto multiple lines when it runs out of space. |
| `Pager` | Swipeable, paged container. |
| `Carousel` | Material 3 carousel container. |
| `Shimmer` | Skeleton/shimmer placeholder wrapper for loading states. |
| `PullToRefresh` | Container with a pull-to-refresh gesture, paired with the `StopRefreshing` event. |
| `AdaptiveVisibility` | Shows or hides its children based on window size class, for adaptive layouts. |
| `LazyTiles` | Self-loading container that fetches its own children from a remote endpoint on first render — and can be forced to reload via `ReloadLazyTiles`. |

**App bars**

| Tile | What it does |
|---|---|
| `TopAppBar` | Screen top bar — title, navigation icon, actions. |
| `BottomAppBar` | Screen bottom bar. |

**Navigation**

| Tile | What it does |
|---|---|
| `Tabs` | Tab row with content switching. |
| `NavigationBar` | Bottom navigation bar. |
| `NavigationRail` | Side navigation rail for larger screens. |
| `NestedNavigationGraph` | Embeds an independent navigation graph / back stack inside a screen. |

**Progress**

| Tile | What it does |
|---|---|
| `CircularProgressIndicator` | Circular, determinate or indeterminate progress. |
| `LinearProgressIndicator` | Linear, determinate or indeterminate progress. |

**Feedback & overlays**

| Tile | What it does |
|---|---|
| `Badge` | Small status/count indicator, usually anchored to another tile. |
| `SearchBar` | Search input with a results/suggestions surface. |
| `Menu` | Dropdown menu, opened and closed via the `ToggleMenu` event. |
| `Popup` | Anchored popup surface, opened and closed via the `TogglePopup` event. |

**System**

| Tile | What it does |
|---|---|
| `SystemBroadcastListener` | Transparent wrapper that renders its children unchanged while listening for system broadcasts — adds no visual layout of its own. |

## What can I do with Mosaic?

57 typed Event schemas. Every event shares `id`, `trigger` (the `EventTrigger` that fires it) and `events` (children dispatched by trigger once this event completes) — which is what makes chaining work: `SendNetworkRequest` → `onSuccess()` → `UpdateData` → `onSuccess()` → `Navigate` is one declarative tree, no glue code. Roughly 70 `EventTrigger` types exist to key that dispatch (`OnClick`, `OnSuccess`, `OnFailure`, `OnStart`, plus scroll, keyboard, lifecycle, permission and network-state triggers).

**Tile management**

| Event | What it does |
|---|---|
| `AddTiles` | Inserts one or more tiles into a container, at the start, end, a specific index, or before/after a given tile ID. |
| `RemoveTiles` | Removes tiles by ID from a container. |
| `UpdateTiles` | Partially updates tile fields via JSON merge, without altering tile types or tree structure. |
| `ReplaceTiles` | Replaces all children of a container with a new list. |
| `WipeTiles` | Removes all children from a container. |
| `ReloadLazyTiles` | Forces a `LazyTiles` tile to re-fetch its content. |
| `CheckIfTileContainsChildren` | Checks whether a container currently holds children with every specified ID; branches `OnSuccess`/`OnFailure`. |
| `GetTileChildrenCount` | Returns the number of direct children of a container tile. |
| `RunEvents` | Transparent container that fires all of its child events unconditionally under one trigger, without transforming `incomingData`. |
| `UpdateEvents` | Partially updates event fields via JSON merge. |

**Navigation**

| Event | What it does |
|---|---|
| `Navigate` | Navigates to a screen within a named navigator. |
| `NavigateUp` | Pops the back stack by one entry. |

**Screen**

| Event | What it does |
|---|---|
| `GetScreen` | Fetches screen content from the server for the current screen ID (doesn't apply it automatically — chain a `ChangeScreenState` on success). |
| `RefreshScreen` | Re-fetches the current screen from the server. |
| `ChangeScreenState` | Transitions the screen to a given state directly, without a network call. |

**Data**

| Event | What it does |
|---|---|
| `SendData` | Stores data in the in-process `DataMailer` under a key, for cross-screen or cross-event retrieval. |
| `CheckForReceivedData` | Reads data from `DataMailer` by key; fires `OnDataReceived` if present. |
| `GetData` | Reads from a data source (in-memory or persisted, plain or segmented) with `Full`/`Single`/`Batch` access. |
| `UpdateData` | Writes or updates data in a data source, including explicit `null` values on in-memory sources. |
| `RemoveData` | Deletes data from a data source — single key, batch, or entire store. |
| `ProcessData` | Passes the current `incomingData` to a registered `DataProcessor` by ID. |
| `TransformData` | Reshapes `incomingData` by applying a template with `<\|path.to.value\|>` placeholders resolved against it. |
| `EvaluateData` | Evaluates a boolean expression against available data and branches `OnSuccess`/`OnFailure` — the mechanism for server-authored conditional logic. |

**Networking**

| Event | What it does |
|---|---|
| `SendNetworkRequest` | Sends an HTTP request with a composed body, headers and query parameters. |
| `DownloadFile` | Downloads a file with progress reporting. |
| `SendFile` | Uploads a file as a raw binary body (no multipart) — built for the signed-URL upload pattern (GCS/S3). |
| `SetIncomingDataToNetworkParamsHolderBody` | Stages `incomingData` as the body of the next network event in the chain. |
| `SetIncomingDataToNetworkParamsHolderHeaders` | Stages `incomingData` as headers, merged into the next network event. |
| `SetIncomingDataToNetworkParamsHolderUrl` | Stages `incomingData` as the URL of the next network event — how a runtime-generated signed URL feeds into `SendFile`. |
| `SetIncomingDataToNetworkParamsHolderQueryParameters` | Stages `incomingData` as query parameters appended to the next network event's URL. |

**File system**

| Event | What it does |
|---|---|
| `SaveFile` | Saves `incomingData` (as `ByteArray`) to the file system. |
| `GetFile` | Reads a file from the file system. |
| `DeleteFile` | Deletes a file by name. |
| `OpenFilePicker` | Opens the system file picker; delivers the selection to child events. |
| `TakePicture` | Opens the device camera via Mosaic's own per-platform `CameraManager` — no third-party picker dependency — with compression/resize options. |
| `GetImageFromGallery` | Opens the device gallery pre-filtered to images; same compression/resize contract as `TakePicture`. |

**Overlays**

| Event | What it does |
|---|---|
| `DisplayDialog` / `DismissDialog` | Shows/dismisses a dialog with a given tile tree as content. |
| `DisplayBottomSheet` / `DismissBottomSheet` | Shows/dismisses a modal bottom sheet. |
| `DisplayNavigationDrawer` / `DismissNavigationDrawer` | Opens/closes the navigation drawer. |
| `DisplaySnackbar` / `DismissSnackbar` | Shows a snackbar (message, duration, optional action) / dismisses it programmatically. |

**Meta, menu & popup**

| Event | What it does |
|---|---|
| `TriggerEvent` | Manually fires another event by its ID. |
| `ToggleMenu` | Toggles the expanded state of a `Menu` tile. |
| `TogglePopup` | Toggles the expanded state of a `Popup` tile. |

**Time & scroll**

| Event | What it does |
|---|---|
| `StartCountdownTimer` | Starts a countdown timer, firing tick and finish events. |
| `ScrollColumnTile` / `ScrollRowTile` / `ScrollPagerTile` | Programmatically scrolls a Column/Row/Pager tile to the top, bottom, or a specific item, optionally animated. |
| `StopRefreshing` | Stops the pull-to-refresh animation on a `PullToRefresh` tile. |

**Security & system**

| Event | What it does |
|---|---|
| `RequestPermission` | Requests one or more runtime permissions using each platform's native mechanism. |
| `BroadcastToSystem` | Sends a named broadcast with an arbitrary payload for cross-screen or host-app communication. |
| `CheckIfHasInternetConnection` | Checks whether the device currently has an active internet connection. |

**Theme**

| Event | What it does |
|---|---|
| `SetTheme` | Overrides the app's Material 3 color scheme (light + dark) globally at runtime, regardless of the current screen. |
| `ResetTheme` | Reverts a `SetTheme` override, restoring the app's default color scheme. |

## Data layer

Two storage tiers, each with plain (flat key-value) and segmented (bucketed) variants:

- **In-memory**, scoped to either the current screen or the whole application session.
- **Persistent**, backed by SQLDelight (multiplatform SQLite).

`GetData`, `UpdateData` and `RemoveData` read and write both tiers with `Full`/`Single`/`Batch` access modes. Values round-trip as real `null`s where the storage tier supports it — a form field that's intentionally empty, or an optional API response field, stays `null` end to end instead of collapsing into "key missing," including when it's serialized straight into a `SendNetworkRequest` body.

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

Custom schemas plug into the same `MosaicSerializer` polymorphic registry and the same `Definition` → `Renderer`/`Runner` lookup as everything shipped in `mosaic-core`.

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
    ├── sample-server   — reference Ktor backend
    └── sample-client   — reference Android/Desktop client
```

## Tech stack

| Concern | Library |
|---|---|
| Language | Kotlin 2.x, Kotlin Multiplatform |
| UI | Compose Multiplatform — Android, iOS, Desktop (JVM), Web (Wasm) |
| Serialization | kotlinx.serialization, fully polymorphic schema registry |
| Networking | Ktor Client / Ktor Server |
| DI | Koin |
| Concurrency | Kotlin Coroutines |
| Local storage | SQLDelight |
| Images | Coil 3 |
| Navigation | Navigation 3 (adaptive layouts, ViewModel-integrated) |

## Running the samples

```bash
# Ktor backend, serves sample screens on :9090
./gradlew sample-server:run

# Desktop client
./gradlew sample-client:run

# Everything
./gradlew build
```

## License

[Apache License 2.0](LICENSE).
