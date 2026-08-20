---
name: mosaic-server
description: |
  Use when writing backend Kotlin DSL code with the Mosaic SDUI framework. Covers: Screen,
  tiles, events, triggers, event chaining, data flow, broadcasts, network requests,
  reusable compositions, and all builder utilities. Trigger whenever the task involves
  building screens, composing tiles, defining event flows, wiring triggers, using
  SendNetworkRequest, handling data sources, creating compositions, or any Mosaic
  server-side (mosaic-server) work.
---

# Mosaic SDUI — Backend DSL Reference

## 1. What is Mosaic

Mosaic is a Server-Driven UI (SDUI) framework for Kotlin Multiplatform. The **server** describes the UI as a tree of typed Kotlin data classes (`TileSchema`, `EventSchema`) and serializes them to JSON. The **client** (Android, iOS, Web, Desktop) deserializes the JSON and renders it via Compose — it has **zero business logic**. All logic, event wiring, and conditional behavior lives in the server DSL.

The server DSL is the only thing you write as a backend developer. Never import client-side types.

For the conceptual "why" behind the framework (module split, mechanisms that make it work), see the `mosaic` skill. For the exhaustive field-by-field reference of every tile/event — full parameter tables, DSL snippets, triggers fired, behavioral notes, for all 48 tiles and 67 events — see `references/tiles.md` and `references/events.md`, bundled alongside this file. This skill teaches the *patterns*; those two files are the *dictionary*.

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

// Two-dimensional alignment (for Box, AsyncImage, Popup)
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
    // weight*/span* only take effect if the tile ends up inside the matching parent
    // (Row/Column for weight, Grid for span) — see the `mosaic` skill's implicit mechanisms section.

    // Spacing — order matters: margin is applied outside background, padding inside border
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

    // Window insets — only one kind per style block
    windowInsets(windowInsetsSystemBars())  // or: windowInsetsStatusBar(), windowInsetsNavigationBar(), windowInsetsIme()
}
```

### Data Sources & Access Modes (`dev.catbit.mosaic.server.builder.data`)

```kotlin
// DataSource helpers
screenPlainData()                          // in-memory, screen-scoped, flat key-value
screenSegmentedData("form")               // in-memory, screen-scoped, segmented
screenNavigationData()                    // read-only data passed via Navigate
applicationPlainData()                     // in-memory, app-wide, flat key-value
applicationSegmentedData("segment")        // in-memory, app-wide, segmented
plainDataBase()                            // persistent flat key-value (local database)
segmentedDataBase("auth")                  // persistent segmented (local database)
tile(tileId = "my_textfield", dataKey = "text")  // read value a tile currently produces

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
slideOverTransition()          // new screen slides over previous (previous stays; exit uses KeepUntilTransitionsFinished)
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
// (note: "Buton" — real typo in the framework's own function names, not a typo in this doc)
```

### Chip helpers (`dev.catbit.mosaic.server.builder.tile.builders.chips`)

```kotlin
// AssistChip / SuggestionChip variant
defaultAssistChip()    elevatedAssistChip()
defaultSuggestionChip() elevatedSuggestionChip()

// FilterChip variant (InputChip has no variant — always flat)
defaultFilterChip()    elevatedFilterChip()
```

### Input helpers (`dev.catbit.mosaic.server.builder.tile.builders.inputs`)

```kotlin
// TextField/DropdownList/DatePicker/TimePicker kind
filledTextField()      outlinedTextField()
outlinedDropdownList()                          // DropdownList has no filled variant helper name difference — see references/tiles.md
outlinedDatePicker()   outlinedTimePicker()

// Keyboard options (TextField only)
keyboardOptions(
    autoCorrectEnabled = true,
    howKeyboardOnFocus = true,             // real param name in the framework (typo: "how", not "show")
    keyboardType = keyboardTypeEmail(),    // keyboardTypeText/Number/Phone/Password/…
    imeAction = keyboardImeActionDone(),   // keyboardImeActionGo/Search/Send/Next/…
    capitalization = keyboardCapitalizationSentences() // None/Words/Characters/Unspecified
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

// note: Grid's `rows` parameter has NO default in the DSL (unlike the schema, which defaults to
// empty) — pass `rows = emptyList()` explicitly if you want implicitly-derived row tracks.
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
// note: height only has 3 levels (Compact/Medium/Expanded) — width has 5. Real asymmetry.
```

### AsyncImage / Image helpers (`dev.catbit.mosaic.server.builder.tile.builders.image`)

```kotlin
// AsyncImage
cropContentScale()        fitContentScale()         fillWidthContentScale()
fillHeightContentScale()  fillBoundsContentScale()  insideContentScale()
urlImageModel(url)        arrayOfBytesImageModel(bytes)   base64ImageModel(base64)

// Image — separate, "image"-prefixed helpers, not shared with AsyncImage despite identical shape
imageCropContentScale()   imageFitContentScale()    imageFillWidthContentScale()
imageFillHeightContentScale()  imageFillBoundsContentScale()  imageInsideContentScale()
```

### Event helpers (`dev.catbit.mosaic.server.builder.event`)

```kotlin
// Tile insertion position (AddTiles)
insertAtStart()             insertAtEnd()
insertAtIndex(3)            insertAfterTile("tile_id")   insertBeforeTile("tile_id")

// Scroll targets
scrollColumnToTop()         scrollColumnToBottom()        scrollColumnTo(index = 5)   // pixel offset on Column, item index on LazyColumn
scrollRowToStart()          scrollRowToEnd()              scrollRowTo(index = 2)      // same pixel/index split, horizontal axis
scrollPageToBegin()         scrollPageToEnd()
scrollPageToNextPage()      scrollPageToPreviousPage()     // no scrollPageTo(index) — only relative moves; clamped, no-op past the ends

// Tile update payloads (for UpdateTiles)
incomingTileUpdateData()                    // uses incomingData as-is (must be a map)
inlineTileUpdateData("field" to value)      // literal Map<String, Any?>
inlineTileUpdateData(mapOf("k" to v))
mappedIncomingTileUpdateData("field" to "<|path|>")   // one template per field, resolved against incomingData

// Data update payloads (for UpdateData — different helpers, don't mix the two families)
incomingUpdateData()                        // spreads incomingData's map keys as separate dataIds
inlineUpdateData("key" to value)
explicitUpdateData(dataId, value)           // writes value as-is under one exact dataId (required if value is itself a map/record)
explicitIncomingUpdateData(dataId)          // writes incomingData as-is under one exact dataId
explicitNullUpdateData(dataId)              // writes null under one exact dataId

// Broadcast data
inlineBroadcastData(data)                  // literal payload for BroadcastToSystem
incomingBroadcastData()                    // forwards incomingData as the broadcast payload

// Screen state
initialState()    successState(data = null)    failureState()

// Navigate options
poppingUpTo(destination = "home", inclusive = false)

// EvaluateData (logical expression helpers)
incomingData()                              // entry point: read incomingData
dataSourceData(source, accessMode)          // entry point: read from a data source
not(expression)
expr1 and expr2       expr1 or expr2        // infix, compose freely
// then a comparison on the Data: .isEqualsTo(x), .isBiggerThan(x), .matchesRegex(r), .containsKey(k),
// .valueAtKey(k) (chainable into a MapOperation), .isWeekend(), etc. — full list in references/events.md's EvaluateData entry.
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
    navigationDrawerTiles = {               // optional — tiles rendered inside a ModalNavigationDrawer
        Column { /* nav items */ }
    },
    ttl = LocalDateTime(2026, 12, 31, 0, 0), // optional — cache expiry; client re-fetches after this datetime
    tiles = {                                // tile tree (trailing lambda)
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
    ttl = LocalDateTime(2026, 12, 31, 0, 0), // optional — cache expiry
    defaultTransition = slideOverTransition(),          // optional
    defaultPopTransition = slideOverTransition(),       // optional — back-stack pop
    defaultPredictivePopTransition = slideOverTransition(), // optional — predictive back gesture
    entries = {
        entry(
            screenId = "login",
            initialTiles = { /* TileSchemaBuilderScope — shown while loading */ },
            initialEvents = {
                // default already wired: GetScreen(onDisplay) → ChangeScreenState(onSuccess)
                // override only when you need custom loading behavior
            },
            failureTiles = { SimpleText(id = "err", text = "Error") }, // shown on Failure state
            failureEvents = { /* EventSchemaBuilderScope — e.g. retry button events */ },
            transition = slideOverTransition(),         // optional — overrides defaultTransition for this entry
            popTransition = slideOverTransition(),      // optional
            predictivePopTransition = slideOverTransition(), // optional
        )
        entry(screenId = "home") // initialEvents default: GetScreen(onDisplay) → ChangeScreenState(onSuccess)
    }
)
```

> **`entry` default `initialEvents`:** automatically wired as `GetScreen(trigger = EventTriggers.onDisplay()) { ChangeScreenState(trigger = EventTriggers.onSuccess(), state = successState()) }`. Override only when custom loading logic is needed. The same default applies to `NestedNavigationGraph`'s own `entry {}` (a tile that hosts its own mini graph — see `references/tiles.md`).

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

Events chain by nesting `events = { }` inside each other — a parent event fires a trigger value when it finishes, and only the children whose own `trigger` matches exactly get to run. `incomingData` flows from parent to matching child.

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
                    expression = incomingData().valueAtKey("email").matchesRegex(".+@.+"),
                    events = {
                        // onSuccess: validation passed
                        SendNetworkRequest(
                            trigger = EventTriggers.onSuccess(),
                            url = "/api/auth/login",
                            method = HttpMethod.POST,
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

Note that `SendNetworkRequest.body` here is left at its default (`null`) — the request just needs the email/password to have reached the server some other way (e.g. cookies), or you'd pass a literal `body = mapOf(...)` built from the earlier `incomingData` via `TransformData` first. There is no `incomingDataBody()` helper — `body` always takes a literal `AnySerializable?` value (or is left to `SetIncomingDataToNetworkParamsHolderBody` staging it from `incomingData` beforehand, see §9).

### Key chaining building blocks

| Concept | What it does |
|---|---|
| `EventTriggers.onSuccess()` / `EventTriggers.onFailure()` | Standard async result triggers — used as the `trigger` of a child event. |
| `EventTriggers.onStart()` | Trigger for a child that should fire before the async operation begins. |
| `EventTriggers.onNetworkResponse(200)` | Fires **instead of** `onSuccess` for that exact HTTP status, but only if some child is wired to it. |
| `EventTriggers.onNetworkFailure(401)` | Fires **instead of** `onFailure` for that exact HTTP status, same condition. |
| `TriggerEvent(trigger, eventId)` | Jump to another event already declared elsewhere in the same screen's tree, by id. |
| `RunEvents(trigger) { }` | Runs its own `events` list unconditionally, in order — a fan-out primitive, not a trigger-filtered chain. |
| `inlineTileUpdateData("key" to value, ...)` | Static update data for `UpdateTiles`. |
| `incomingTileUpdateData()` | Uses `incomingData` as the update map (must be `Map<String, Any?>`). |
| `inlineBroadcastData(data)` | Static payload for `BroadcastToSystem`. |
| `incomingBroadcastData()` | Uses `incomingData` as the broadcast payload. |

### `incomingData` — what it is

`incomingData` is the data passed from a parent event to its matching children via `onTrigger(trigger, data)`. Each event declares what it passes:
- `GetData` → `onSuccess`: the assembled read result (shape depends on access modes — see `references/events.md`)
- `SendNetworkRequest` → `onSuccess`/`onFailure`: the parsed response body
- `TransformData` → `onSuccess`: the resolved template output
- `EvaluateData` → `onSuccess`/`onFailure`: the same `incomingData` it received, forwarded unchanged

`TransformData` is used to reshape `incomingData` before passing it downstream:

```kotlin
TransformData(
    trigger = EventTriggers.onSuccess(),
    template = mapOf(
        "email" to "<|email|>",       // dot-path into incomingData map
        "raw" to "<||>",              // <||> = entire incomingData as-is (any type)
        "asText" to "<//>"            // <//> = entire incomingData coerced to String
    ),
    events = {
        // incomingData is now the resolved template
    }
)
```

Placeholder rules — two delimiters, same path syntax:
- `<|path.to.key|>` — pipe delimiter, dot-notation path into incomingData
- `<|items[0].name|>` — array index access
- `<||>` — entire incomingData, native type preserved (Int, Boolean, List, etc.)
- `</path.to.key/>` — slash delimiter, same path syntax, but **always** coerces to `String`, even as the whole template string
- `<//>` — entire incomingData coerced to `String`. Use this instead of `<||>` when the destination expects a `String` (e.g. a tile's `text` field via `UpdateTiles`' `mappedIncomingTileUpdateData`) but the source value isn't naturally one — `<||>` there would throw a decoding exception when the tile field decodes.
- Single-placeholder template with `<|...|>` → native type preserved; with `</.../>` → always String; placeholder mixed with surrounding text (either delimiter) → coerced to String
- A placeholder that resolves to a missing key, wrong-typed navigation, or out-of-range index throws — and a placeholder resolving to `null` **inside mixed text** also throws (only a bare `</value/>` on its own renders the literal string `"null"`).

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
        // Read from persistent local database
        reading(
            dataSource = segmentedDataBase("auth"),
            accessMode = batchAccessMode(listOf("sessionCookie"))
        )
    },
    events = {
        // incomingData here is the assembled result of all 3 readings
    }
)
```

### Writing data

`update(dataSource, updateData)` — `updateData` is `incomingUpdateData()`/`inlineUpdateData(...)` (spreads a map's keys into separate dataIds) or `explicitIncomingUpdateData(dataId)`/`explicitUpdateData(dataId, value)` (writes the value as-is under one exact dataId — required whenever the value is itself a record/map, e.g. a segmented-data row).

```kotlin
UpdateData(
    trigger = EventTriggers.onSuccess(),
    updates = {
        // Explicit: writes the whole incomingData map intact under one dataId
        update(
            dataSource = segmentedDataBase("action_planning_$planId"),
            updateData = explicitIncomingUpdateData(dataId = "step_01")
        )
    }
)
```

### Storage layers

| Layer | DataSource helper | Scope | Persistent |
|---|---|---|---|
| Application plain memory | `applicationPlainData()` | Whole app session | No |
| Application segmented memory | `applicationSegmentedData("segment")` | Whole app session | No |
| Screen plain memory | `screenPlainData()` | Current screen | No |
| Screen segmented memory | `screenSegmentedData("segment")` | Current screen | No |
| Navigation data | `screenNavigationData()` | Current screen | **Read-only** |
| Tile value | `tile("id", "key")` | Current screen | **Read-only** |
| Persistent flat | `plainDataBase()` | All screens | Yes (local database) |
| Persistent segmented | `segmentedDataBase("segment")` | All screens | Yes (local database) |
| Cross-screen one-shot | `DataMailer` via `SendData`/`CheckForReceivedData` | App-global, consume-once | No |

**Rule:** always use the same segment key when writing and reading segmented data — `screenSegmentedData("form")` and `segmentedDataBase("auth")` are independent namespaces from each other too, not interchangeable.

`UpdateData`/`RemoveData` targeting navigation data, a tile, or an inline source are silently ignored — those three are not writable through this mechanism.

---

## 8. Broadcasts

### Screen-scoped broadcast (control tiles within a screen)

Used to send commands to tiles (e.g., scroll a Column):

```kotlin
ScrollColumnTile(
    trigger = EventTriggers.onSuccess(),
    tileId = "my_list",
    where = scrollColumnToTop(),
    smoothly = true
)
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

`onSystemBroadcast` fires only when `broadcastId` matches exactly (structural equality — see the `mosaic` skill's event-chaining mechanism section).

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

### Headers every request arrives with

The client stamps **every** outgoing request — `SendNetworkRequest`, `UploadFile`, all 3 download events, `GetScreen`/`RefreshScreen`, the initial-graph fetch, the cache-version check, with no exceptions and no way for the DSL author to opt out — with 9 `x-mosaic-*` headers describing the device/platform, via a Ktor client plugin installed once on the single `HttpClient` every one of those operations shares:

| Header | Content |
|---|---|
| `x-mosaic-platform-name` | Platform name, e.g. `"Android"`, `"iOS"`, `"JVM"`, `"WasmJS"`. |
| `x-mosaic-device` | Device model identifier, platform-specific format. |
| `x-mosaic-os-version` | Operating system version string. |
| `x-mosaic-extra-info` | Extra platform-specific key-value pairs, flattened to a comma-separated `key=value` list — not JSON; parse by splitting the string. |
| `x-mosaic-screen-size` | `"WIDTHxHEIGHT"`, e.g. `"1920x1080"`. |
| `x-mosaic-screen-density` | Screen density as a decimal string, e.g. `"1.0"`. |
| `x-mosaic-locale` | Current locale, e.g. `"pt-BR"`. |
| `x-mosaic-timezone` | `"offset\|timezoneId"`, e.g. `"GMT+09:30\|Australia/Darwin"`. |
| `x-mosaic-dark-mode` | `"true"`/`"false"` string. |

Because these arrive on literally every request — including the very first one, the initial-graph fetch, before any DSL-authored logic runs — a backend can branch on device/platform/locale/theme (serve a different `Screen` payload per locale, adapt copy for dark mode, gate a feature by OS version, etc.) without the DSL author ever declaring that intent explicitly. There's no DSL-level representation of these headers on the server side (no `EventTrigger`/data source reads them) — they're a pure backend-side concern, read directly off the HTTP request the same way any other header would be.

### Authenticated request pattern

This pattern injects a session cookie from the persistent database as a header before the request fires, as a reusable event composition:

```kotlin
fun EventSchemaBuilderScope.SendAuthenticatedNetworkRequest(
    trigger: EventTrigger,
    url: String,
    method: HttpMethod,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    SendNetworkRequest(
        trigger = trigger,
        url = url,
        method = method,
        events = {
            events()
            // Before the request fires: read session cookie, inject as header
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

Key events for pre-populating requests (all fire on `EventTriggers.onSuccess()` after the value was staged, `onFailure()` if the incoming value didn't validate):
- `SetIncomingDataToNetworkParamsHolderHeaders(trigger)` — stages `incomingData` (must be a map with at least one `String` value) as headers for the **next** network event.
- `SetIncomingDataToNetworkParamsHolderBody(trigger)` — stages `incomingData` (any non-null value) as the body.
- `SetIncomingDataToNetworkParamsHolderUrl(trigger)` — stages `incomingData` (must be a `String`) as the URL — the main use case is feeding `UploadFile.url`, which is the only network event with a nullable `url`.
- `SetIncomingDataToNetworkParamsHolderQueryParameters(trigger)` — stages `incomingData` (must be a map) as query parameters.

**Important:** the staged value is consumed by **whichever** network event runs next — `SendNetworkRequest`, `UploadFile`, any of the 3 download events, or even `GetScreen`/`RefreshScreen`/`Navigate` (screen loads also consume it). Stage immediately before the one event it's meant for, never earlier in a longer chain.

**Dispatch rules (`SendNetworkRequest`, and `UploadFile` for its own outcome triggers):**
- `onSuccess` fires for 2xx responses **without** a matching `onNetworkResponse(code)` child.
- `onNetworkResponse(200)` fires instead, only if some child is wired to it.
- `onFailure` fires for non-2xx and network exceptions, **without** a matching `onNetworkFailure(code)` child (network exceptions always fall through to `onFailure`, never to a status-specific trigger — there was no HTTP response to have a status).
- `onNetworkFailure(401)` fires instead, only if some child is wired to it.

---

## 10. Reusable Compositions

### Tile composition

Extension function on `TileSchemaBuilderScope`. This is how you create reusable tile components:

```kotlin
fun TileSchemaBuilderScope.EnvironmentCard(
    id: String,
    name: String,
    onEditClick: EventSchemaBuilderScope.() -> Unit,
    onDeleteClick: EventSchemaBuilderScope.() -> Unit
) {
    Column(
        id = id,
        searchableTerms = listOf(name)  // enables an ancestor's filterChildrenByTerm to find this
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
            val menuId = "menu_$id"
            SimpleText(text = name, style = { size(width = weightHorizontally(1f), height = wrapVertically()) })
            Menu(
                id = menuId,
                items = {
                    addMenuItem(id = "${menuId}_edit", label = "Edit", leadingIcon = icon("edit"))
                    addMenuItem(id = "${menuId}_delete", label = "Delete", leadingIcon = icon("delete"))
                },
                events = {
                    RunEvents(
                        trigger = EventTriggers.onMenuItemClick("${menuId}_edit"),
                        events = {
                            ToggleMenu(trigger = EventTriggers.inline(), menuId = menuId)
                            onEditClick()   // caller-injected events
                        }
                    )
                    RunEvents(
                        trigger = EventTriggers.onMenuItemClick("${menuId}_delete"),
                        events = {
                            ToggleMenu(trigger = EventTriggers.inline(), menuId = menuId)
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
            EnvironmentCard(
                id = "env_${env.id}",
                name = env.name,
                onEditClick = { Navigate(trigger = EventTriggers.inline(), destination = "edit_env", navigatorId = "main") },
                onDeleteClick = { SendNetworkRequest(trigger = EventTriggers.inline(), url = "/api/env/${env.id}", method = HttpMethod.DELETE) }
            )
        }
    }
}
```

### Event composition

Extension function on `EventSchemaBuilderScope`, useful to bundle a recurring event pattern (auth headers, standard error handling) under one call:

```kotlin
fun EventSchemaBuilderScope.GetJson(
    trigger: EventTrigger,
    url: String,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    SendNetworkRequest(
        trigger = trigger,
        url = url,
        method = HttpMethod.GET,
        headers = mapOf("Accept" to "application/json"),
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
weightHorizontally(1f)         // flex weight (only inside Row/FlowRow children)
fillVertically()
wrapVertically()
fixedVertically(48)
weightVertically(1f)           // flex weight (only inside Column children)

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

3. **Never use `snapshotLocals()` inside lambdas.** Call it BEFORE passing the lambda to `addBuilder()`. Doing it inside reads locals after the `CompositionLocalProvider` (which backs the server-side `BuildContext` mechanism) has already cleaned up.

4. **Always use `randomId()` for programmatically generated event IDs** — especially in reusable compositions where multiple instances may appear in the same tree.

5. **Segmented data: use the same segment key in both read and write.** `screenSegmentedData("form")` and `segmentedDataBase("auth")` are independent namespaces from each other.

6. **`filterChildrenByTerm` filtering on Column/Row/LazyColumn/LazyRow/Grid/FlexBox/FlowRow:** children with `searchableTerms = null` are excluded whenever a filter term is active. Only children with a non-null `searchableTerms` list are ever shown while filtering (case-insensitive substring match).

7. **`TriggerEvent`/`UpdateEvents` look up the target by id across the whole screen's tile tree** — but a `TilesManager` never searches into a child `NestedNavigationGraph`'s own screens, only upward toward its own parent screen. Targeting an event that lives inside a nested graph from outside it will not resolve.

8. **`EventRunner`s are suspend functions on the client** — this is client implementation detail, but it explains why chained events "just work" without you writing thread-hopping code in the DSL. Nothing on the server side needs to account for this.

9. **`UpdateTiles` with `incomingTileUpdateData()`**: the `incomingData` must be `Map<String, Any?>`. If the value might be a scalar, either use `TransformData` first to wrap it (`mapOf("field" to "<||>")`), or use `mappedIncomingTileUpdateData("field" to "<||>")` directly on the `update(...)` — no separate `TransformData` step needed. Either way, if the target tile field is `String` (e.g. `text`) but the resolved value isn't (e.g. an `Int` tick from `StartCountdownTimer`), use the `</.../>` delimiter instead of `<|...|>` (e.g. `"text" to "<//>"`) — otherwise the tile patch fails to decode.

10. **`EvaluateData` is complex** — it's a recursive sealed-expression tree. Study a couple of real examples in `references/events.md`'s `EvaluateData` entry before generating expressions with it.

11. **Every `EventTriggers.onXxx()` call is a plain value comparison, not a subscription.** A child only runs if its `trigger` is *structurally equal* to what the parent fired — a typo in a trigger name, or the wrong parameter on a parameterized trigger (`onNetworkResponse(200)` vs `onNetworkResponse(201)`), compiles fine and silently never fires. There's no compile-time or runtime check tying a parent's declared outcomes to what a child listens for.

---

## 13. See Also

For exhaustive field lists, DSL snippets and full trigger tables — the source of truth this skill's examples are drawn from:

- **`references/tiles.md`** (bundled with this skill) — every one of the 48 tile schemas: parameters, defaults, DSL snippet, triggers fired, behavioral notes.
- **`references/events.md`** (bundled with this skill) — every one of the 67 event schemas: parameters, defaults, DSL snippet, triggers fired, behavioral notes.
- **`mosaic` skill** — the mechanics behind everything in this skill (trigger matching, the data/cache layer, explicit/implicit mechanisms) — read this when you need to know *why*, not just *how*.
- **`mosaic-client` skill** — the client side of the framework: custom `TileRenderer`/`EventRunner`, and which of the mechanisms referenced above are actually reachable from third-party client code.

**Keeping this skill in sync:** any change to a Tile/Event's schema, builder, or behavior must update this file's affected example/table **and** the matching entry in `references/tiles.md` or `references/events.md` **and** the schema's own KDoc, in the same change — never just one of the three.
