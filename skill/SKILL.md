---
name: mosaic
description: >
  Mosaic SDUI (Server-Driven UI) framework — use this skill for ANY task involving Mosaic:
  adding or modifying Tiles (UI components), adding or modifying Events (interactions),
  writing backend Kotlin DSL code with builders, understanding the client rendering pipeline,
  navigating the module structure, or generating boilerplate. Trigger on mentions of Tiles,
  Events, EventTriggers, Schemas, Builders, Renderers, Runners, Holders, MosaicApplication,
  GraphBuilder, ScreenBuilder, TilesManager, EventManager, or any SDUI work in this KMP project.
---

# Mosaic — SDUI Framework

Mosaic is a **Server-Driven UI (SDUI)** framework built with Kotlin Multiplatform. The backend defines the entire UI structure as a tree of typed components (**Tiles**) and interactions (**Events**), serializes it to JSON, and sends it over HTTP. The client deserializes and renders it with Compose Multiplatform — no app release required.

---

## Module Structure

| Module | Role |
|---|---|
| `mosaic-core` | Shared contracts: all **Schemas** (Tile + Event), `MosaicSerializer` for polymorphic JSON. Used by both server and client. |
| `mosaic-server` | Type-safe Kotlin **DSL** for building UI trees. Contains Builders for Tiles, Events, Screens, and Graphs. |
| `mosaic-client` | UI **rendering engine**. Deserializes JSON → `TileSchema` trees → Compose. Executes `EventSchema` logic via `EventRunner`s. |
| `sample-server` | Reference Ktor server. Shows how to serve SDUI screens. |
| `sample-client` | Reference client app (Android + Desktop). Entry: `MosaicApplication(baseUrl, colorScheme)`. |

---

## Core Pattern: Schema → Builder → Renderer

Every component follows this three-layer pattern:

| Layer | Module | Kotlin type | Role |
|---|---|---|---|
| **Schema** | `mosaic-core` | `data class` | Typed JSON contract. `@Serializable`, `@SerialName`. Registered in `MosaicSerializer`. |
| **Builder** | `mosaic-server` | `class` | DSL class. Extension function on `TileSchemaBuilderScope` or `EventSchemaBuilderScope`. |
| **Definition** | `mosaic-client` | `object` | Binds Schema KClass to Renderer + HolderBuilder. Registered in `MosaicModules`. |
| **Renderer** | `mosaic-client` | `object` | `@Composable` that receives a Schema and renders Compose UI. |
| **Holder** | `mosaic-client` | `class` | Stateful wrapper around a Schema. Manages updates + child hierarchy. |
| **HolderBuilder** | `mosaic-client` | `object` | Factory: produces a Holder from a Schema. |
| **Runner** | `mosaic-client` | `object` | Executes an EventSchema's logic inside an `EventRunningScope`. |

---

## Critical Rules (Always Apply)

1. **Read existing examples first.** Before writing any code, read an existing implementation of the same type. For Events: `TriggerEventEvent*`. For Tiles: `ButtonTile*`.

2. **Never invent from scratch.** Mirror existing patterns exactly — type choices (`object` vs `class`), inheritance, package structure.

3. **Type conventions:**
   - `Definition`, `Runner`, `HolderBuilder` → `object` (stateless singletons)
   - `Holder` → `class` (stateful instance)

4. **EventRunner body** → leave a `println()` or `TODO()`. Do not write final business logic.

5. **TileRenderer body** → leave a `println()` or `TODO()` in the `Render` composable. Do not attempt final UI.

6. **Register every new Schema in `MosaicSerializer`** (both Tile and Event subclasses). Omitting causes runtime deserialization failure.

7. **For non-obvious scenarios, present the approach and wait for approval** before generating code.

---

## Workflow: Adding a New Tile

1. **`mosaic-core`** — Create `[Name]TileSchema : TileSchema`. Add `@Serializable`, `@SerialName("Name")`. Register in `MosaicSerializer`.
2. **`mosaic-server`** — Create `[Name]TileSchemaBuilder` + extension function `TileSchemaBuilderScope.[Name](...)`.
3. **`mosaic-client`** — Under `ui/sdui/implementations/tile/tiles/[category]/[name]/`:
   - `[Name]TileDefinition` (object)
   - `[Name]TileRenderer` (object — placeholder `Render`)
   - `[Name]TileHolder` (class)
   - `[Name]TileHolderBuilder` (object)
4. Register the `Definition` in `MosaicModules`.

## Workflow: Adding a New Event

1. **`mosaic-core`** — Create `[Name]EventSchema : EventSchema`. Add `@Serializable`, `@SerialName("Name")`. Register in `MosaicSerializer`.
2. **`mosaic-server`** — Create `[Name]EventBuilder` + extension function `EventSchemaBuilderScope.[Name](...)`.
3. **`mosaic-client`** — Under `ui/sdui/implementations/event/events/[category]/[name]/`:
   - `[Name]EventDefinition` (object)
   - `[Name]EventRunner` (object — placeholder body)
   - `[Name]EventHolder` (class)
   - `[Name]EventHolderBuilder` (object)
4. Register the `Definition` in `MosaicModules`.

---

## Critical DSL Rules

**Triggers:** ALWAYS use `EventTriggers.xxx()` — never instantiate trigger classes directly.
```kotlin
// CORRECT:
Navigate(trigger = EventTriggers.onClick(), ...)
// WRONG:
Navigate(trigger = OnClickEventTrigger(), ...)  // ← compile error
```
Exception: `OnPullEventTrigger` is not in `EventTriggers` — use it as a direct object reference.

**Graph DSL:** uses `entries = { }` as a named parameter:
```kotlin
Graph(startEntryId = "splash", entries = {
    SplashEntry()
    HomeEntry()
})
```

**Events on tiles:** `events = { }` named parameter, not trailing lambda:
```kotlin
Button("Go", events = {
    Navigate(trigger = EventTriggers.onClick(), destination = "home", navigatorId = "root")
})
```

**UpdateTiles:** scope-based, not list-based:
```kotlin
UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
    update("btn-id", mapOf("loading" to false, "enabled" to true))
})
```

**GetData:** scope-based readings:
```kotlin
GetData(trigger = EventTriggers.onClick(), readings = {
    reading(dataSource = segmentedDataBase("auth"), accessMode = singleAccessMode("token"))
}, events = { /* chain here */ })
```

**GetData + tile readings → merged map:** use `fullAccessMode()` to get a proper map. Each `tile(tileId, dataKey)` reading with `fullAccessMode()` contributes `{dataKey: value}` to the accumulated map:
```kotlin
readings = {
    reading(tile("email", "email"), fullAccessMode())     // → {"email": "..."}
    reading(tile("password", "password"), fullAccessMode()) // → merged {"email":"..","password":".."}
}
// incomingData = Map{"email": "..", "password": ".."}
```

**SendNetworkRequest body:** `incomingData` is NOT used automatically. To pass data as the request body or headers, use `SetIncomingDataToNetworkParamsHolderBody` / `SetIncomingDataToNetworkParamsHolderHeaders` before the network event:
```kotlin
SetIncomingDataToNetworkParamsHolderBody(trigger = EventTriggers.onSuccess(), events = {
    SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/login", method = HttpMethod.POST)
    // ^ body comes from the holder (set by the event above)
})
```

**Style/Size:** `size()` takes named width/height. `padding()` and `margin()` have NO single-argument overload — use named parameters:
```kotlin
style = {
    size(width = fillHorizontally(), height = fillVertically())
    size(width = fillHorizontally(max = 400), height = fixedVertically(56))
    padding(horizontal = 16, vertical = 16)     // ← NOT padding(16)
    margin(horizontal = 24, top = 0, bottom = 8)
    windowInsets(windowInsetsSystemBars())
}
```

**Size behaviors with parent requirements:**
- `weightHorizontally(1f)` → only works inside a `Row`; ignored elsewhere
- `spanHorizontally(2)` → only works inside a `Grid`; ignored elsewhere
- `flexHorizontally(...)` → only works inside a `FlexBox`; ignored elsewhere
- `weightVertically(1f)` → only works inside a `Column`; ignored elsewhere
- `fillRowHeight()` → only works inside a `FlowRow`; ignored elsewhere

**`clip` vs `border.radius`:** `clip(roundedCornerShape(8))` clips both content AND background to the shape. `border(radius=...)` only rounds the border stroke — it does NOT clip the background. To get a rounded background, always use `clip()`.

**Typography quirk:** `typographyLabelLarge/Medium/Small()` map to `bodyLarge/Medium/Small` on the client — they do NOT produce Material label styles. Use body variants directly if predictable sizing matters.

**UpdateData helpers:** use `inlineUpdateData()` and `incomingUpdateData()`:
```kotlin
UpdateData(trigger = EventTriggers.onDataUpdated(), updates = {
    addUpdate(screenPlainData(), inlineUpdateData("key" to value))
    addUpdate(screenPlainData(), incomingUpdateData())  // ← not UpdateDataEventSchema.UpdateDate.Incoming
})
```

**Quirky trigger names (double "On"):** These are correct as written:
```kotlin
EventTriggers.onOnCountdownTimerTick()    // ← double "On" is intentional
EventTriggers.onOnCountdownTimerFinish()  // ← double "On" is intentional
EventTriggers.onOnSelectChanged()         // ← double "On" is intentional
```

**Parameterized triggers:**
```kotlin
EventTriggers.onScrolled(OnScrolledEventTrigger.ScrollDirection.Down)  // requires direction
EventTriggers.onPageChanged(OnPageChangedEventTrigger.Direction.Forward) // requires direction
EventTriggers.onNetworkResponse(401)   // requires HTTP code
EventTriggers.onMenuItemClick("item-id")
```

## Backend DSL Quick Start

```kotlin
// In a Ktor route handler:
Graph(
    startEntryId = "splash",
    entries = {
        SplashScreenEntry()
        HomeScreenEntry()
    }
)

// Defined as extension functions on GraphEntryBuilderScope:
fun GraphEntryBuilderScope.SplashScreenEntry() {
    entry(
        screenId = "splash",
        initialTiles = {
            Column(
                style = {
                    size(width = fillHorizontally(), height = fillVertically())
                    windowInsets(windowInsetsSystemBars())
                },
                alignment = alignHorizontallyToCenter(),
                arrangement = arrangeVerticallySpacedBy(space = 24, alignment = alignVerticallyToCenter())
            ) {
                Image(resourceName = "ic_logo", style = { size(width = fixedHorizontally(100), height = fixedVertically(100)) })
                CircularProgressIndicator(style = { size(width = fixedHorizontally(40), height = fixedVertically(40)) })
            }
        },
        initialEvents = {
            GetData(
                trigger = EventTriggers.onDisplay(),
                readings = {
                    reading(dataSource = segmentedDataBase("auth"), accessMode = singleAccessMode("sessionCookie"))
                },
                events = {
                    Navigate(trigger = EventTriggers.onSuccess(), navigatorId = "root", destination = "home",
                        popUpTo = poppingUpTo(destination = "splash", inclusive = true))
                    Navigate(trigger = EventTriggers.onFailure(), navigatorId = "root", destination = "login",
                        popUpTo = poppingUpTo(destination = "splash", inclusive = true))
                }
            )
        }
    )
}
```

Events chain via the `events = { }` parameter. Each child event fires when its `trigger` matches a dispatch from the parent. `incomingData` flows through the chain carrying results. See `references/event-chaining.md` for deep patterns.

---

## EventRunningScope API (available inside every Runner)

```kotlin
// Tile manipulation
tilesEditor.addTiles(tileSchemas, groupingTileId)
tilesEditor.removeTiles(tileIds, groupingTileId)
tilesEditor.updateTiles(tileId, data)
tilesEditor.wipeTiles(groupingTileId)

// Overlays
tilesOverlaysEditor.setDialogTiles(tiles)
tilesOverlaysEditor.dismissDialog()
tilesOverlaysEditor.setBottomSheetTiles(tiles)
tilesOverlaysEditor.dismissBottomSheet()

// Data
dataHolder.getPlainData(key) / getSegmentedData(key)
tilesValueProducer.getValueWithKey(tileId, key)
get<DataMailer>().sendData(data, dataKey)

// Navigation
get<NavigatorsHolder>()[navigatorId]?.navigate(destination, data)
get<NavigatorsHolder>()[navigatorId]?.navigateUp()

// Event chaining
onTrigger(EventTrigger, data)           // triggers child events
runEventInline(eventSchema, data)       // executes another event

// Coroutines
runSuspendOnScreenScope { /* suspend ops */ }
runSuspendOnStateHolderScope { /* state updates */ }

// Logging
log(level, msg) / logError(throwable, tag)
```

---

## Reference Files

Load these **on demand** — only read what the current task needs:

| File | Load when... |
|---|---|
| `references/architecture.md` | Understanding the full data flow, module dependencies, rendering pipeline |
| `references/builders-guide.md` | Working with StyleSchemaBuilder, GraphBuilder, ScreenBuilder, transitions, CompositionLocals |
| `references/foundation.md` | Understanding TilesManager, EventManager, TileHolder, OverlayContainer, MosaicModules DI |
| `references/triggers-catalog.md` | Choosing which EventTrigger to use for a tile or event |
| `references/tiles/text.md` | SimpleTextTileSchema |
| `references/tiles/buttons.md` | Button, IconButton, FloatingActionButton |
| `references/tiles/inputs.md` | TextField, Checkbox, Switch, RadioButton |
| `references/tiles/layout.md` | Box, Column, Row, FlexBox, LazyColumn, LazyRow, Grid, FlowRow |
| `references/tiles/containers.md` | Pager, Carousel, Card, Shimmer, PullToRefresh, LazyTiles |
| `references/tiles/images.md` | Image, AsyncImage, Icon |
| `references/tiles/progress.md` | LinearProgressIndicator, CircularProgressIndicator |
| `references/tiles/navigation.md` | NavigationBar, NavigationRail, Tabs, NestedNavigationGraph, AdaptiveNavigation |
| `references/tiles/appbars.md` | TopAppBar, BottomAppBar |
| `references/tiles/misc.md` | Badge, SuggestionChip, SearchBar, Menu |
| `references/events/tile-management.md` | AddTiles, RemoveTiles, ReplaceTiles, UpdateTiles, WipeTiles, ReloadLazyTiles |
| `references/events/navigation.md` | Navigate, NavigateUp |
| `references/events/networking.md` | SendNetworkRequest, DownloadFile |
| `references/events/data.md` | GetData, SendData, UpdateData, RemoveData, ProcessData, CheckForReceivedData, TransformData, EvaluateData |
| `references/events/overlays.md` | DisplayDialog, DismissDialog, DisplayBottomSheet, DismissBottomSheet, DisplaySnackbar, DismissSnackbar, NavigationDrawer |
| `references/events/screen.md` | GetScreen, ChangeScreenState, RefreshScreen |
| `references/events/scroll.md` | ScrollColumn, ScrollRow, ScrollPager |
| `references/events/file.md` | SaveFile, GetFile, DeleteFile |
| `references/events/misc.md` | RequestPermission, CheckIfHasInternetConnection, ToggleMenu, StartCountdownTimer, StopRefreshing, TriggerEvent, UpdateEvents |
| `references/event-chaining.md` | **Deep patterns**: chaining mechanics, incomingData flow, multi-level chains, form validation, loading states, pagination, cross-screen data |
