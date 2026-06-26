---
name: mosaic
description: |
  Use when writing backend Kotlin DSL code with the Mosaic SDUI framework. Covers: Screen,
  tiles, events, triggers, event chaining, data flow, broadcasts, network requests,
  reusable compositions, and all builder utilities. Trigger whenever the task involves
  building screens, composing tiles, defining event flows, wiring triggers, using
  SendNetworkRequest, handling DataHolder, creating compositions, or any Mosaic server-side work.
---

# Mosaic SDUI — Backend DSL Reference

## 1. What is Mosaic

Mosaic is a Server-Driven UI (SDUI) framework for Kotlin Multiplatform. The **server** describes the UI as a tree of typed Kotlin data classes (`TileSchema`, `EventSchema`) and serializes them to JSON. The **client** (Android, iOS, Web, Desktop) deserializes the JSON and renders it via Compose — it has **zero business logic**. All logic, event wiring, and conditional behavior lives in the server DSL.

The server DSL is the only thing you write as a backend developer. Never import client-side types.

---

## 2. Essential Imports

```kotlin
// Core package root
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.randomId

// Screen builder
import dev.catbit.mosaic.server.builder.screen.Screen    // top-level fun Screen(...)
import dev.catbit.mosaic.server.builder.graph.Graph       // top-level fun Graph(...)

// Color
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColor*  // all theme color helpers

// Icon
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.icon.outlinedIcon
import dev.catbit.mosaic.server.builder.icon.roundedIcon

// Placement
import dev.catbit.mosaic.server.builder.placement.align*
import dev.catbit.mosaic.server.builder.placement.arrange*

// Typography
import dev.catbit.mosaic.server.builder.typography.typography*

// Data sources and access modes
import dev.catbit.mosaic.server.builder.data.screenPlainData
import dev.catbit.mosaic.server.builder.data.screenSegmentedData
import dev.catbit.mosaic.server.builder.data.screenNavigationData
import dev.catbit.mosaic.server.builder.data.plainDataBase
import dev.catbit.mosaic.server.builder.data.segmentedDataBase
import dev.catbit.mosaic.server.builder.data.tile
import dev.catbit.mosaic.server.builder.data.fullAccessMode
import dev.catbit.mosaic.server.builder.data.singleAccessMode
import dev.catbit.mosaic.server.builder.data.batchAccessMode

// Tile builders (examples; full list in references/tiles.md)
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField

// Event builders (examples; full list in references/events.md)
import dev.catbit.mosaic.server.builder.event.builders.data.GetData
import dev.catbit.mosaic.server.builder.event.builders.data.UpdateData
import dev.catbit.mosaic.server.builder.event.builders.data.EvaluateData
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.event.builders.networking.SendNetworkRequest
import dev.catbit.mosaic.server.builder.event.builders.event.RunEvents
import dev.catbit.mosaic.server.builder.event.builders.event.TriggerEvent
import dev.catbit.mosaic.server.builder.event.builders.system.BroadcastToSystem

// Scopes (used in extension functions for compositions)
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

// HTTP
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
```

---

## 3. Builder Utilities

> **CRITICAL RULE — Always use utility functions. Never use raw schema types or enum values directly.**
>
> ✅ `filledButton()` ✅ `color("#FF5722")` ✅ `typographyTitleMedium()` ✅ `alignHorizontallyToCenter()`
> ❌ `ButtonTileSchema.Type.FILLED` ❌ `ColorSchema.Hex("FF5722")` ❌ `TypographySchema.TITLE_MEDIUM`
>
> Every builder subpackage exposes top-level helper functions for this exact purpose. All helpers below are importable via the wildcard `import dev.catbit.mosaic.server.builder.<subpackage>.*`.

---

### Color (`dev.catbit.mosaic.server.builder.color`)

```kotlin
color("#FF5722")                         // hex (AARRGGBB or RRGGBB)
color(r = 1f, g = 0.3f, b = 0f)        // RGBA floats (0.0–1.0), alpha defaults to 1f
color(themeColorPrimary())              // Material3 theme token

// All theme color helpers:
themeColorPrimary()           themeColorOnPrimary()
themeColorPrimaryContainer()  themeColorOnPrimaryContainer()
themeColorSecondary()         themeColorOnSecondary()
themeColorSecondaryContainer() themeColorOnSecondaryContainer()
themeColorTertiary()          themeColorOnTertiary()
themeColorTertiaryContainer() themeColorOnTertiaryContainer()
themeColorError()             themeColorOnError()
themeColorErrorContainer()    themeColorOnErrorContainer()
themeColorBackground()        themeColorOnBackground()
themeColorSurface()           themeColorOnSurface()
themeColorSurfaceVariant()    themeColorOnSurfaceVariant()
themeColorOutline()           themeColorOutlineVariant()
themeColorScrim()
themeColorInverseSurface()    themeColorInverseOnSurface()    themeColorInversePrimary()
themeColorSurfaceDim()        themeColorSurfaceBright()
themeColorSurfaceContainerLowest()  themeColorSurfaceContainerLow()
themeColorSurfaceContainer()        themeColorSurfaceContainerHigh()
themeColorSurfaceContainerHighest()
```

### Icon (`dev.catbit.mosaic.server.builder.icon`)

```kotlin
icon("settings")                                   // material symbol name, defaults: size=24, style=OUTLINED
icon("delete", color = color(themeColorError()), size = 20, style = roundedIcon())
outlinedIcon()   roundedIcon()   sharpIcon()
```

### Placement (`dev.catbit.mosaic.server.builder.placement`)

```kotlin
// Horizontal alignment
alignHorizontallyToStart()  alignHorizontallyToCenter()  alignHorizontallyToEnd()

// Vertical alignment
alignVerticallyToTop()  alignVerticallyToCenter()  alignVerticallyToBottom()

// Two-dimensional alignment (for Box, AsyncImage)
alignToTopStart()   alignToTopCenter()   alignToTopEnd()
alignToCenterStart() alignToCenter()     alignToCenterEnd()
alignToBottomStart() alignToBottomCenter() alignToBottomEnd()

// Arrangements
arrangeVerticallyToTop()     arrangeVerticallyToBottom()
arrangeVerticallySpacedBy(8) // optional: alignment = alignVerticallyToTop()
arrangeHorizontallyToStart() arrangeHorizontallyToEnd()
arrangeHorizontallySpacedBy(12)
arrangeToCenter()  arrangeSpaceEvenly()  arrangeSpaceBetween()  arrangeSpaceAround()
```

### Typography (`dev.catbit.mosaic.server.builder.typography`)

```kotlin
typographyDisplayLarge()   typographyDisplayMedium()   typographyDisplaySmall()
typographyHeadlineLarge()  typographyHeadlineMedium()  typographyHeadlineSmall()
typographyTitleLarge()     typographyTitleMedium()     typographyTitleSmall()
typographyBodyLarge()      typographyBodyMedium()      typographyBodySmall()
typographyLabelLarge()     typographyLabelMedium()     typographyLabelSmall()
```

### Style — inside `style { }` block

```kotlin
style = {
    // Size
    size(
        width = fillHorizontally(),        // fill available width (optional max: fillHorizontally(200))
        height = wrapVertically()          // wrap content height
    )
    // Also: wrapHorizontally(), fixedHorizontally(80), weightHorizontally(1f), spanHorizontally(2)
    //       fillVertically(), fixedVertically(48), weightVertically(1f), fillRowHeight(1f)

    // Spacing
    padding(horizontal = 16, vertical = 8)
    padding(top = 8, end = 16, bottom = 8, start = 16)
    margin(horizontal = 16, top = 4, bottom = 4)

    // Background
    background(color(themeColorSurface()))

    // Border
    border(color = color(themeColorOutline()), thickness = 1, radius = radius(all = 12))

    // Clip / Shape
    clip(circleShape())
    clip(roundedCornerShape(all = 16))
    clip(roundedCornerShape(topStart = 16, topEnd = 16, bottomStart = 0, bottomEnd = 0))
    clip(rectangleShape())

    // Window insets
    windowInsets(windowInsetsSystemBars())  // or: windowInsetsStatusBar(), windowInsetsNavigationBar(), windowInsetsIme()
}
```

### Data Sources & Access Modes (`dev.catbit.mosaic.server.builder.data`)

```kotlin
// DataSource helpers
screenPlainData()                          // in-memory, screen-scoped, flat key-value
screenSegmentedData("form")               // in-memory, screen-scoped, segmented
screenNavigationData()                    // read-only data passed via Navigate
plainDataBase()                            // persistent flat key-value (SQLite)
segmentedDataBase("auth")                  // persistent segmented (SQLite)
tile(tileId = "my_textfield", dataKey = "text")  // read value from tile

// AccessMode helpers
fullAccessMode()                           // returns all data as map
singleAccessMode("email")                  // one value by key
batchAccessMode(listOf("email", "name"))   // multiple values; allowMissingData=false, unwrapValuesToList=false
batchAccessMode(listOf("sessionCookie"), allowMissingData = true)
```

### Animation Transitions (`dev.catbit.mosaic.server.builder.animation`)

```kotlin
slideInFromRightTransition()   slideInFromLeftTransition()
slideInFromBottomTransition()  slideInFromTopTransition()
slideOutToRightTransition()    slideOutToLeftTransition()
slideOutToBottomTransition()   slideOutToTopTransition()
slideHorizontalTransition()    slideVerticalTransition()
fadeTransition()               fadeAndSlideHorizontalTransition()
slideOverTransition()          // new screen slides over previous (previous stays)
// Each accepts optional: animationSpec = AnimationSpecSchema.Tween() or AnimationSpecSchema.Spring()
```

### Visibility (`dev.catbit.mosaic.server.builder.tile`)

```kotlin
visible()     // TileSchema.Visibility.VISIBLE   — shown and takes space
invisible()   // TileSchema.Visibility.INVISIBLE — hidden but still takes space
gone()        // TileSchema.Visibility.GONE      — removed from layout entirely
```

### Button helpers (`dev.catbit.mosaic.server.builder.tile.builders.buttons`)

```kotlin
// Button type
filledButton()       elevatedButton()       filledTonalButton()
outlinedButton()     textButton()

// Button shape
roundedButton()      squareButton()

// Icon position
iconAtStart()        iconAtEnd()

// IconButton type
filledIconButton()   filledTonalIconButton()   outlinedIconButton()   defaultIconButton()

// FloatingActionButton size
defaultFloatingActionButon()   mediumFloatingActionButon()   largeFloatingActionButon()
```

### Chip helpers (`dev.catbit.mosaic.server.builder.tile.builders.chips`)

```kotlin
// AssistChip / SuggestionChip variant
defaultAssistChip()    elevatedAssistChip()
defaultSuggestionChip() elevatedSuggestionChip()

// FilterChip variant
defaultFilterChip()    elevatedFilterChip()
```

### TextField helpers (`dev.catbit.mosaic.server.builder.tile.builders.inputs`)

```kotlin
// Kind
filledTextField()      outlinedTextField()

// Keyboard options (compose via keyboardOptions { })
keyboardOptions(
    keyboardType = keyboardTypeEmail(),    // keyboardTypeText/Number/Phone/Password/…
    imeAction = keyboardImeActionDone(),   // keyboardImeActionGo/Search/Send/Next/…
    capitalization = keyboardCapitalizationSentences(), // None/Words/Characters/Unspecified
    autoCorrect = true
)

// Visual transformation
keyboardVisualTransformationNone()
keyboardVisualTransformationPassword()
keyboardVisualTransformationCustom(mask = "###-###")
```

### Pager / Carousel helpers (`dev.catbit.mosaic.server.builder.tile.builders.grouping`)

```kotlin
pageFill()               // page fills viewport
pageFixed(200)           // page has fixed dp width

multiBrowse(preferredItemWidth = 240, minSmallItemWidth = 40, maxSmallItemWidth = 80)
uncontained(itemWidth = 200)
```

### Grid helpers (`dev.catbit.mosaic.server.builder.tile.builders.grouping`)

```kotlin
// Column/row track definitions
gridColumnFixed(120)        gridColumnFlexible(1f)    gridColumnFraction(0.5f)
gridColumnAuto()            gridColumnMinContent()    gridColumnMaxContent()
gridRowFixed(48)            gridRowFlexible(1f)       /* same variants */

// Grid flow
flowGridThroughRows()       flowGridThroughColumns()
```

### FlexBox helpers (`dev.catbit.mosaic.server.builder.tile.builders.grouping`)

```kotlin
flexDirectionRow()          flexDirectionColumn()
flexDirectionRowReverse()   flexDirectionColumnReverse()
flexJustifyStart()          flexJustifyCenter()         flexJustifyEnd()
flexJustifySpaceBetween()   flexJustifySpaceAround()    flexJustifySpaceEvenly()
flexAlignItemsStart()       flexAlignItemsCenter()      flexAlignItemsEnd()
flexAlignItemsStretch()     flexAlignItemsBaseline()
flexAlignContentStart()     flexAlignContentCenter()    flexAlignContentEnd()
flexAlignContentStretch()   flexAlignContentSpaceBetween() flexAlignContentSpaceAround()
flexWrap()                  flexNoWrap()                flexWrapReverse()
```

### AdaptiveVisibility helpers (`dev.catbit.mosaic.server.builder.tile.builders.grouping`)

```kotlin
widthVisibleFromCompact()   widthVisibleFromMedium()   widthVisibleFromExpanded()
widthVisibleFromLarge()     widthVisibleFromExtraLarge()
widthVisibleUntilCompact()  widthVisibleUntilMedium()  widthVisibleUntilExpanded()
widthVisibleUntilLarge()    widthVisibleUntilExtraLarge()

heightVisibleFromCompact()  heightVisibleFromMedium()  heightVisibleFromExpanded()
heightVisibleUntilCompact() heightVisibleUntilMedium() heightVisibleUntilExpanded()
```

### AsyncImage helpers (`dev.catbit.mosaic.server.builder.tile.builders.media`)

```kotlin
cropContentScale()        fitContentScale()         fillWidthContentScale()
fillHeightContentScale()  fillBoundsContentScale()  insideContentScale()
```

### Event helpers (`dev.catbit.mosaic.server.builder.event`)

```kotlin
// Tile insertion position (AddTiles)
insertAtStart()             insertAtEnd()
insertAtIndex(3)            insertAfterTile("tile_id")   insertBeforeTile("tile_id")

// Scroll targets
scrollColumnToTop()         scrollColumnToBottom()        scrollColumnTo(index = 5)
scrollRowToStart()          scrollRowToEnd()              scrollRowTo(index = 2)
scrollPageToBegin()         scrollPageToEnd()
scrollPageToNextPage()      scrollPageToPreviousPage()

// Data payloads
incomingData()                              // use incomingData as the update/broadcast value
inlineTileUpdateData("field" to value)      // Map<String, Any?> for UpdateTiles
inlineTileUpdateData(mapOf("k" to v))
incomingTileUpdateData()                    // pass incomingData directly to UpdateTiles
inlineUpdateData("key" to value)            // for UpdateData
incomingUpdateData()

// Broadcast data
inlineBroadcastData(data)                  // send a value as broadcast payload
incomingBroadcastData()                    // forward incomingData as broadcast

// Screen state
initialState()    successState(data)    failureState()

// Navigate options
poppingUpTo(screenId = "home", inclusive = false)

// EvaluateData (logical expression helpers — import from event builder)
not(expression)
dataSourceData(source, accessMode)        // read data inside expression context
```

---

## 4. Screen Structure

```kotlin
fun myScreen() = Screen(
    id = "my_screen",                        // used by Navigate(destination = "my_screen")
    events = {                               // screen-level events (run on display etc.)
        GetData(
            trigger = EventTriggers.onDisplay(),
            // ...
        )
    },
    tiles = {                                // tile tree
        Column(id = "root") {
            SimpleText(id = "title", text = "Hello")
        }
    }
)
```

For navigation graphs (multiple screens with back-stack):

```kotlin
fun myGraph() = Graph(
    startEntryId = "login",
    defaultTransition = slideOverTransition(),
    entries = {
        entry(
            screenId = "login",
            initialTiles = { /* TileSchemaBuilderScope */ },
            initialEvents = { /* EventSchemaBuilderScope — onDisplay etc. */ },
            failureTiles = { SimpleText(id = "err", text = "Error") }
        )
        entry(screenId = "home", initialTiles = { /* ... */ })
    }
)
```

---

## 5. DSL Hierarchy

```
Screen / Graph.Entry
  ├── tiles { TileSchemaBuilderScope }
  │     └── Column { ... }
  │           ├── events { EventSchemaBuilderScope }   ← events on THIS tile
  │           │     └── SendNetworkRequest(
  │           │             trigger = EventTriggers.onClick(),
  │           │             events = {               ← child events (chained after parent)
  │           │                 UpdateTiles(trigger = EventTriggers.onSuccess(), ...)
  │           │             }
  │           │         )
  │           └── Button(id = "btn") { ... }           ← child tiles (for container tiles)
  └── events { EventSchemaBuilderScope }               ← screen-level events
```

- **`events { }` inside a tile**: events owned by that tile, triggered by user interaction with it.
- **`events = { }` parameter inside an event**: child events, executed after the parent event fires a matching trigger.
- **`trigger = EventTriggers.onClick()`**: this event fires when the parent tile/context fires `onClick`.
- Child events execute **sequentially** and all run in the ViewModel coroutine scope.

---

## 6. Event Chaining — Most Important Pattern

Events chain by nesting `events = { }` inside each other. The `incomingData` flows from parent to child.

```kotlin
// Pattern: GetData → EvaluateData → SendNetworkRequest → UpdateTiles
Button(id = "btn_login", text = "Login") {
    events = {
        GetData(
            trigger = EventTriggers.onClick(),
            readings = {
                reading(
                    dataSource = tile("tf_email", "text"),
                    accessMode = singleAccessMode("email")
                )
                reading(
                    dataSource = tile("tf_password", "text"),
                    accessMode = singleAccessMode("password")
                )
            },
            events = {
                EvaluateData(
                    trigger = EventTriggers.onSuccess(),
                    expression = /* ... */,
                    events = {
                        // onSuccess: validation passed
                        SendNetworkRequest(
                            trigger = EventTriggers.onSuccess(),
                            url = "/api/auth/login",
                            method = HttpMethod.POST,
                            body = incomingDataBody(),    // uses incomingData as body
                            events = {
                                Navigate(
                                    trigger = EventTriggers.onSuccess(),
                                    destination = "home",
                                    navigatorId = "main"
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update(
                                            tileId = "btn_login",
                                            updateData = inlineTileUpdateData("loading" to false)
                                        )
                                    }
                                )
                            }
                        )
                        // onFailure: validation failed
                        UpdateTiles(
                            trigger = EventTriggers.onFailure(),
                            updates = {
                                update(
                                    tileId = "tf_email",
                                    updateData = inlineTileUpdateData(
                                        "state" to "ERROR",
                                        "supportingText" to "Email inválido"
                                    )
                                )
                            }
                        )
                    }
                )
            }
        )
    }
}
```

### Key chaining APIs

| API | Purpose |
|---|---|
| `onSuccess { }` / `onFailure { }` | Standard async result triggers |
| `onStart { }` | Fired before async operation begins |
| `onNetworkResponse(200) { }` | Specific HTTP status code (replaces onSuccess for that code) |
| `onNetworkFailure(401) { }` | Specific HTTP failure code (replaces onFailure for that code) |
| `TriggerEvent(trigger, eventId)` | Jump to another event by ID anywhere in the tree |
| `RunEvents(trigger) { }` | Group multiple events under one trigger without changing `incomingData` |
| `inlineTileUpdateData("key" to value, ...)` | Static update data for `UpdateTiles` |
| `incomingTileUpdateData()` | Uses `incomingData` as update map (must be `Map<String, Any>`) |
| `inlineBroadcastData(data)` | Static payload for `BroadcastToSystem` |
| `incomingBroadcastData()` | Uses `incomingData` as broadcast payload |

### `incomingData` — what it is

`incomingData` is the data passed from a parent event to its children via `onTrigger(trigger, data)`. Each event declares what it passes:
- `GetData` → `onSuccess`: the map of read data (`Map<String, Any>`)
- `SendNetworkRequest` → `onSuccess`: the parsed response body
- `TransformData` → `onSuccess`: the resolved template output
- `EvaluateData` → `onSuccess`/`onFailure`: no data change (passes through)

`TransformData` is used to reshape `incomingData` before passing it downstream:

```kotlin
TransformData(
    trigger = EventTriggers.onSuccess(),
    template = mapOf(
        "email" to "<|email|>",       // dot-path into incomingData map
        "raw" to "<||>"               // <||> = entire incomingData as-is (any type)
    ),
    events = {
        // incomingData is now the resolved template
    }
)
```

Placeholder rules:
- `<|path.to.key|>` — dot-notation path into incomingData
- `<|items[0].name|>` — array index access
- `<||>` — entire incomingData (preserves native type: Int, Boolean, List, etc.)
- Single-placeholder template → native type preserved; placeholder mixed with text → coerced to String

---

## 7. Data Flow

### Reading data

```kotlin
GetData(
    trigger = EventTriggers.onClick(),
    readings = {
        // Read from a TextField tile
        reading(
            dataSource = tile("tf_search", "text"),
            accessMode = singleAccessMode("query")
        )
        // Read from screen-scoped memory
        reading(
            dataSource = screenSegmentedData("form"),
            accessMode = batchAccessMode(listOf("email", "name"))
        )
        // Read from persistent DB
        reading(
            dataSource = segmentedDataBase("auth"),
            accessMode = batchAccessMode(listOf("sessionCookie"))
        )
    },
    events = {
        // incomingData here is Map<String, Any> merging all readings
    }
)
```

### Writing data

```kotlin
UpdateData(
    trigger = EventTriggers.onSuccess(),
    updates = {
        update(
            dataSource = screenSegmentedData("form"),
            incomingData = true   // write incomingData under this source
        )
    }
)
```

### Storage layers

| Layer | DataSource helper | Scope | Persistent |
|---|---|---|---|
| Screen plain memory | `screenPlainData()` | Current screen ViewModel | No |
| Screen segmented memory | `screenSegmentedData("segment")` | Current screen ViewModel | No |
| Navigation data | `screenNavigationData()` | Current screen ViewModel | **Read-only** |
| Tile value | `tile("id", "key")` | Current screen ViewModel | **Read-only** |
| Persistent flat | `plainDataBase()` | All screens | Yes (SQLite) |
| Persistent segmented | `segmentedDataBase("segment")` | All screens | Yes (SQLite) |
| Cross-screen in-memory | `DataMailer` via `SendData`/`CheckForReceivedData` | App-global | No |

**Rule:** Always use the same segment key when writing and reading segmented data.

---

## 8. Broadcasts

### Screen-scoped broadcast (control tiles within a screen)

Used to send commands to tiles (e.g., scroll a Column):

```kotlin
// Server-side: the event that emits
ScrollColumnTile(
    trigger = EventTriggers.onSuccess(),
    tileId = "my_list",
    where = ScrollColumnTile.Where.Top,
    smoothly = true
)
// This internally calls EventRunningScope.broadcastData(ColumnTileScreenTilesBroadcastData.ScrollToTop(...))
```

### System broadcast (app-wide, cross-screen)

Used to trigger events on tiles in **other screens** by a named `broadcastId`:

```kotlin
// Emit (from any screen)
BroadcastToSystem(
    trigger = EventTriggers.onSuccess(),
    broadcastId = "ENVIRONMENT_CHANGE",
    data = incomingBroadcastData()   // or: inlineBroadcastData(mapOf("key" to "value"))
)

// Receive (on any tile, any screen)
Column(id = "root") {
    events = {
        UpdateTiles(
            trigger = EventTriggers.onSystemBroadcast("ENVIRONMENT_CHANGE"),
            updates = { /* ... */ }
        )
    }
}
```

`OnSystemBroadcast` fires only when `broadcastId` matches exactly.

---

## 9. Network Requests

### Basic request

```kotlin
SendNetworkRequest(
    trigger = EventTriggers.onClick(),
    url = "/api/environments",
    method = HttpMethod.GET,
    headers = mapOf("Accept" to "application/json"),
    events = {
        UpdateTiles(
            trigger = EventTriggers.onSuccess(),
            updates = { /* incomingData = response body */ }
        )
        DisplaySnackbar(
            trigger = EventTriggers.onFailure(),
            message = "Erro de conexão"
        )
        // Handle specific HTTP codes:
        Navigate(
            trigger = EventTriggers.onNetworkFailure(401),
            destination = "login",
            navigatorId = "main"
        )
    }
)
```

### Authenticated request pattern (from VirtosOne)

This pattern injects a session cookie from the persistent DB as a header before the request fires:

```kotlin
fun EventSchemaBuilderScope.SendAuthenticatedNetworkRequest(
    trigger: EventTrigger,
    networkRequestData: NetworkRequestData,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    SendNetworkRequest(
        trigger = trigger,
        url = networkRequestData.url,
        method = networkRequestData.method,
        body = networkRequestData.body,
        events = {
            events()
            // Before request fires: read session cookie, inject as header
            GetData(
                trigger = EventTriggers.onStart(),
                readings = {
                    reading(
                        dataSource = segmentedDataBase("auth"),
                        accessMode = batchAccessMode(listOf("sessionCookie"))
                    )
                },
                events = {
                    SetIncomingDataToNetworkParamsHolderHeaders(
                        trigger = EventTriggers.onSuccess()
                    )
                }
            )
            // On 401: trigger token refresh
            TriggerEvent(
                trigger = EventTriggers.onNetworkFailure(401),
                eventId = "refresh_token_event_id"
            )
        }
    )
}
```

Key events for pre-populating requests:
- `SetIncomingDataToNetworkParamsHolderHeaders(trigger)` — injects `incomingData` (`Map<String, String>`) as request headers
- `SetIncomingDataToNetworkParamsHolderBody(trigger)` — injects `incomingData` as request body
- `SetIncomingDataToNetworkParamsHolderUrl(trigger)` — injects `incomingData` (String) as request URL (used with `SendFile`)

**Dispatch rules:**
- `onSuccess` fires for 2xx responses **without** a matching `onNetworkResponse(code)` child
- `onNetworkResponse(200)` overrides `onSuccess` for that code
- `onFailure` fires for non-2xx and network exceptions (no specific child)
- `onNetworkFailure(401)` overrides `onFailure` for 401 (never fires for exceptions)

---

## 10. Reusable Compositions

### Tile composition

Extension function on `TileSchemaBuilderScope`. This is how you create reusable tile components:

```kotlin
// Definition (e.g., Environment.kt in compositions/tiles/)
fun TileSchemaBuilderScope.Environment(
    environment: EnvironmentModel,
    onEditClick: EventSchemaBuilderScope.() -> Unit,
    onDeleteClick: EventSchemaBuilderScope.() -> Unit
) {
    Column(
        id = "ENVIRONMENT#${environment.id}",
        searchableTerms = listOf(environment.name)  // enables parent filterChildrenByTerm
    ) {
        Row(
            style = {
                size(width = fillHorizontally(), height = wrapVertically())
                padding(horizontal = 16, vertical = 8)
                background(color(themeColorSurface()))
                clip(roundedCornerShape(16))
            },
            arrangement = arrangeHorizontallySpacedBy(8),
            alignment = alignVerticallyToCenter()
        ) {
            val menuId = "MENU_${environment.id}"
            Menu(
                id = menuId,
                items = {
                    addMenuItem(id = "${menuId}_EDIT", label = "Editar", leadingIcon = icon("edit"))
                    addMenuItem(id = "${menuId}_DELETE", label = "Excluir", leadingIcon = icon("delete"))
                },
                events = {
                    RunEvents(
                        trigger = EventTriggers.onMenuItemClick("${menuId}_EDIT"),
                        events = {
                            ToggleMenu(trigger = EventTriggers.onClick(), menuId = menuId)
                            onEditClick()   // caller-injected events
                        }
                    )
                    RunEvents(
                        trigger = EventTriggers.onMenuItemClick("${menuId}_DELETE"),
                        events = {
                            ToggleMenu(trigger = EventTriggers.onClick(), menuId = menuId)
                            onDeleteClick()
                        }
                    )
                }
            ) {
                IconButton(icon = icon("more_vert"), events = {
                    ToggleMenu(trigger = EventTriggers.onClick(), menuId = menuId)
                })
            }
        }
    }
}

// Usage
tiles = {
    LazyColumn(id = "env_list") {
        environments.forEach { env ->
            Environment(
                environment = env,
                onEditClick = { Navigate(trigger = EventTriggers.inline(), destination = "edit_env", navigatorId = "main") },
                onDeleteClick = { SendNetworkRequest(trigger = EventTriggers.inline(), url = "/api/env/${env.id}", method = HttpMethod.DELETE) }
            )
        }
    }
}
```

### Event composition

Extension function on `EventSchemaBuilderScope`:

```kotlin
fun EventSchemaBuilderScope.SendNetworkRequest(
    trigger: EventTrigger,
    id: String = randomId(),
    networkRequestData: NetworkRequestData,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    SendNetworkRequest(
        id = id,
        trigger = trigger,
        url = networkRequestData.url,
        method = networkRequestData.method,
        body = networkRequestData.body,
        headers = networkRequestData.headers,
        events = events
    )
}
```

---

## 11. Style Utilities (Quick Reference)

```kotlin
// Inside style = { } block:

// Sizes
fillHorizontally(max = 400)    // fill up to max dp
wrapHorizontally()
fixedHorizontally(120)
weightHorizontally(1f)         // flex weight (for Row children)
fillVertically()
wrapVertically()
fixedVertically(48)
weightVertically(1f)           // flex weight (for Column children)

// Radius shorthand
radius(all = 12)
radius(topStart = 16, topEnd = 16, bottomStart = 0, bottomEnd = 0)

// Shapes
circleShape()
rectangleShape()
roundedCornerShape(all = 8)
roundedCornerShape(topStart = 12, topEnd = 12, bottomStart = 0, bottomEnd = 0)

// Window insets
windowInsets(windowInsetsSystemBars())
windowInsets(windowInsetsIme())
windowInsets(windowInsetsNavigationBar())
```

---

## 12. Critical Rules

1. **Always use utility functions — never raw enum/schema values.** Use `filledButton()` not `ButtonTileSchema.Type.FILLED`. Use `color("#hex")` not `ColorSchema.Hex(...)`. Use `typographyTitleMedium()` not `TypographySchema.TITLE_MEDIUM`. Use `alignHorizontallyToCenter()` not `AlignmentSchema.Horizontal.Center`. All helpers are listed in **Section 3** above, grouped by package.

2. **Never import builder classes — only import extension functions and top-level helpers.** The `ButtonTileSchemaBuilder` class is `internal`. The correct import is `import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button` (the extension fun), or use a wildcard `import dev.catbit.mosaic.server.builder.tile.builders.buttons.*`.

4. **Never use `snapshotLocals()` inside lambdas.** Call it BEFORE passing the lambda to `addBuilder()`. Doing it inside reads locals after the `CompositionLocalProvider` has already cleaned up.

5. **Always use `randomId()` for programmatically generated event IDs** — especially in reusable compositions where multiple instances may appear in the same tree.

6. **Segmented data: use the same segment key in both read and write.** `screenSegmentedData("form")` and `segmentedDataBase("auth")` are independent namespaces.

7. **`filterChildrenByTerm` filtering on Column/LazyColumn:** Children with `searchableTerms = null` are always shown regardless of the active filter. Only children with a non-null `searchableTerms` list are filtered (case-insensitive contains check).

8. **`TriggerEvent` requires the target event to be in the same tile's event tree.** You cannot target events on other tiles by ID unless they are registered at screen level.

9. **Event runners are suspend functions** — they run in the ViewModel scope. Use `withContext(Dispatchers.IO)` inside runners for blocking work. Don't manually dispatch to threads in DSL code.

10. **`UpdateTiles` with `incomingTileUpdateData()`**: the `incomingData` must be `Map<String, Any>`. If the value might be a scalar, use `TransformData` first to wrap it: `mapOf("field" to "<||>")`.

11. **`EvaluateData` is complex** — always study the existing `EvaluateDataEventBuilder` before generating expressions with it. It has a recursive sealed-interface expression tree.

---

## 13. Keeping This Skill Up-to-Date

**Rito obrigatório:** sempre que uma mudança no projeto afetar tiles, events, triggers, mecanismos ou qualquer comportamento coberto por esta skill, atualize **imediatamente**:

1. `skill/mosaic/SKILL.md` — regras, imports, utilities e exemplos centrais
2. `skill/mosaic/references/tiles.md` — campos, triggers, notas e exemplos de cada tile
3. `skill/mosaic/references/triggers.md` — DSL, SerialName e lista de "Fired By" de cada trigger
4. `skill/mosaic/references/events.md` — campos e child triggers de cada event
5. `skill/mosaic/references/mechanisms.md` — APIs de EventRunningScope, DataHolder, etc.
6. `.claude/context/tiles-catalog.md` — tabela de referência rápida dos tiles
7. `.claude/context/events-catalog.md` — tabela de referência rápida dos events
8. `.claude/context/triggers-catalog.md` — tabela de referência rápida dos triggers

Não finalize nenhuma tarefa que altere comportamento do framework sem passar por essa checklist.

---

## 14. See Also

When you need exhaustive field lists, supported trigger tables, or mechanism APIs:

- **`references/tiles.md`** — all 41 tile schemas: fields, defaults, supported triggers
- **`references/events.md`** — all events: fields, child triggers fired
- **`references/triggers.md`** — all 72 triggers with DSL signatures
- **`references/mechanisms.md`** — EventRunningScope API, DataHolder, TilesEditor, etc. (for implementing custom EventRunners/TileRenderers)
