# Mosaic — Tiles Catalog

> Atualizado: jun/2026. Validado contra TileSchema, TileRenderer e TileHolder de cada tile.

All tiles implement `TileSchema`. Every tile inherits these base fields:

| Field | Type | Description |
|---|---|---|
| `id` | `String` | Unique identifier within the screen |
| `events` | `List<EventSchema>?` | Events attached to this tile |
| `style` | `StyleSchema` | Size, margin, padding, background, border, clip, windowInsets |
| `visibility` | `TileSchema.Visibility` | `VISIBLE`, `INVISIBLE` (takes space), `GONE` (no space) |
| `searchableTerms` | `List<String>?` | Terms used by parent container's `filterChildrenByTerm` to include/exclude this tile. `null` = always shown |

JSON serialization uses `@SerialName` as the `type` discriminator field.

---

## Text

### SimpleTextTileSchema
**JSON type:** `"Text"`

| Field | Type | Default |
|---|---|---|
| `text` | `String` | required |
| `color` | `ColorSchema?` | `null` (falls back to `LocalTextStyle` color) |
| `typography` | `TypographySchema?` | `null` (falls back to `LocalTextStyle`) |

`ColorSchema`: `{ "argb": "#AARRGGBB" }`
`TypographySchema`: `{ "fontSize": Int, "fontWeight": Int (100–900), "letterSpacing": Float? }`

**Supported triggers:** none

---

## Buttons

### ButtonTileSchema
**JSON type:** `"Button"`

| Field | Type | Default |
|---|---|---|
| `text` | `String` | required |
| `icon` | `IconSchema?` | `null` |
| `buttonType` | `Type` | `FILLED` |
| `shape` | `Shape` | `ROUNDED` |
| `loading` | `Boolean` | `false` |
| `enabled` | `Boolean` | `true` |
| `iconPosition` | `IconPosition` | `START` |

`Type`: `FILLED`, `ELEVATED`, `FILLED_TONAL`, `OUTLINED`, `TEXT`
`Shape`: `SQUARE`, `ROUNDED`
`IconPosition`: `START`, `END`

**Note:** When `loading = true`, shows `CircularProgressIndicator` instead of text/icon.

**Supported triggers:** `OnClick`, `OnLongPress`

---

### IconButtonTileSchema
**JSON type:** `"IconButton"`

| Field | Type | Default |
|---|---|---|
| `icon` | `IconSchema` | required |
| `buttonType` | `Type` | `DEFAULT` |
| `enabled` | `Boolean` | required |

`Type`: `DEFAULT`, `FILLED`, `FILLED_TONAL`, `OUTLINED`

**Supported triggers:** `OnClick`

---

### FloatingActionButtonTileSchema
**JSON type:** `"FloatingActionButton"`

| Field | Type | Default |
|---|---|---|
| `icon` | `IconSchema` | required |
| `size` | `Size` | required |
| `enabled` | `Boolean` | required |

`Size`: `DEFAULT` → `SmallFloatingActionButton`, `MEDIUM` → `FloatingActionButton`, `LARGE` → `LargeFloatingActionButton`

**Supported triggers:** `OnClick`

---

## Chips

### AssistChipTileSchema
**JSON type:** `"AssistChip"`

| Field | Type | Default |
|---|---|---|
| `text` | `String` | required |
| `leadingIcon` | `IconSchema?` | `null` |
| `trailingIcon` | `IconSchema?` | `null` |
| `enabled` | `Boolean` | required |
| `variant` | `Variant` | `DEFAULT` |

`Variant`: `DEFAULT` → `AssistChip`, `ELEVATED` → `ElevatedAssistChip`

**Use case:** action chip with optional icons on both ends. Suitable as menu trigger (leading icon + label + chevron trailing icon).

**Supported triggers:** `OnClick`

---

### FilterChipTileSchema
**JSON type:** `"FilterChip"`

| Field | Type | Default |
|---|---|---|
| `text` | `String` | required |
| `selected` | `Boolean` | required |
| `leadingIcon` | `IconSchema?` | `null` |
| `trailingIcon` | `IconSchema?` | `null` |
| `enabled` | `Boolean` | required |
| `variant` | `Variant` | `DEFAULT` |

`Variant`: `DEFAULT` → `FilterChip`, `ELEVATED` → `ElevatedFilterChip`

**Note:** When `selected = true`, M3 renders a filled container; `leadingIcon` can visually indicate selection (e.g. a checkmark icon).

**Supported triggers:** `OnCheck`, `OnUncheck`, `OnCheckChanged`

---

### InputChipTileSchema
**JSON type:** `"InputChip"`

| Field | Type | Default |
|---|---|---|
| `text` | `String` | required |
| `selected` | `Boolean` | required |
| `leadingIcon` | `IconSchema?` | `null` |
| `trailingIcon` | `IconSchema?` | `null` (typically a close/remove icon) |
| `enabled` | `Boolean` | required |

**Note:** No `variant` — `InputChip` has no elevated variant in Material 3. `trailingIcon` is purely visual; no separate trailing-icon click callback.

**Supported triggers:** `OnCheck`, `OnUncheck`, `OnCheckChanged`

---

### SuggestionChipTileSchema
**JSON type:** `"SuggestionChip"`

| Field | Type | Default |
|---|---|---|
| `text` | `String` | required |
| `icon` | `IconSchema?` | `null` |
| `enabled` | `Boolean` | required |
| `variant` | `Variant` | `DEFAULT` |

`Variant`: `DEFAULT` → `SuggestionChip`, `ELEVATED` → `ElevatedSuggestionChip`

**Supported triggers:** `OnClick`

---

## Inputs

### TextFieldTileSchema
**JSON type:** `"TextFieldTileSchema"` (class name, no @SerialName override)

| Field | Type | Default |
|---|---|---|
| `value` | `String` | required |
| `enabled` | `Boolean` | required |
| `leadingIcon` | `IconSchema?` | — |
| `clickableLeadingIcon` | `Boolean` | — |
| `trailingIcon` | `IconSchema?` | — |
| `clickableTrailingIcon` | `Boolean` | — |
| `prefixText` | `String?` | — |
| `suffixText` | `String?` | — |
| `placeholder` | `String?` | — |
| `label` | `String?` | — |
| `supportingText` | `String?` | — |
| `minLines` | `Int` | — |
| `maxLines` | `Int` | — |
| `kind` | `Kind` | — |
| `state` | `State` | — |
| `keyboardOptions` | `KeyboardOptions?` | — |
| `visualTransformation` | `VisualTransformation?` | — |

`Kind`: `FILLED`, `OUTLINED`
`State`: `NORMAL`, `ERROR`
`VisualTransformation`: `None`, `Password`, `Custom(mask: String)`
`KeyboardOptions.ImeAction`: `Unspecified`, `Default`, `None`, `Go`, `Search`, `Send`, `Previous`, `Next`, `Done`
`KeyboardOptions.KeyboardType`: `Unspecified`, `Text`, `Ascii`, `Number`, `Phone`, `Uri`, `Email`, `Password`, `NumberPassword`, `Decimal`
`KeyboardOptions.KeyboardCapitalization`: `Unspecified`, `None`, `Characters`, `Words`, `Sentences`

**Note:** Renderer holds local `TextFieldValue` state. `LaunchedEffect` watches `schema.value` and syncs when server pushes update (cursor moved to end). `OnTextChanged` fires only when new text differs from previously known value.

**Supported triggers:** `OnTextChanged`, `OnKeyboardDone`, `OnKeyboardGo`, `OnKeyboardNext`, `OnKeyboardPrevious`, `OnKeyboardSearch`, `OnKeyboardSend`, `OnLeadingIconClick` (when `clickableLeadingIcon = true`), `OnTrailingIconClick` (when `clickableTrailingIcon = true`)

---

### CheckboxTileSchema
**JSON type:** `"Checkbox"`

| Field | Type |
|---|---|
| `checked` | `Boolean` |
| `enabled` | `Boolean` |

**Note:** Fully controlled component. Dispatch order on change: directional trigger (`OnCheck`/`OnUncheck`) → `OnCheckChanged` → local `CheckboxTileEvents.OnCheckChanged` (updates holder state).

**Supported triggers:** `OnCheck`, `OnUncheck`, `OnCheckChanged`

---

### RadioButtonTileSchema
**JSON type:** `"RadioButton"`

| Field | Type |
|---|---|
| `selected` | `Boolean` |
| `enabled` | `Boolean` |
| `groupId` | `String` |

**Note:** On tap, fires both `OnSelectEventTrigger` and group-level `RadioButtonTileGroupEvents.OnRadioSelected` (carrying id and groupId), allowing sibling radios with the same groupId to deselect client-side without a server round-trip.

**Supported triggers:** `OnSelect`

---

### SwitchTileSchema
**JSON type:** `"Switch"`

| Field | Type |
|---|---|
| `checked` | `Boolean` |
| `enabled` | `Boolean` |

**Note:** Fully controlled component with identical trigger semantics to `CheckboxTileSchema`.

**Supported triggers:** `OnCheck`, `OnUncheck`, `OnCheckChanged`

---

## Images and Icons

### IconTileSchema
**JSON type:** `"Icon"`

| Field | Type |
|---|---|
| `icon` | `IconSchema` |

`IconSchema`: `{ "name": "material_symbol_name" }`

**Supported triggers:** none

---

### ImageTileSchema
**JSON type:** `"Image"`

| Field | Type | Default |
|---|---|---|
| `resourceName` | `String` | required — key in `DrawableResourcesHolder` map |
| `contentDescription` | `String?` | `null` |
| `contentScale` | `ContentScale` | — |
| `alpha` | `Float` | — |
| `alignment` | `AlignmentSchema.TwoDimensional` | — |

`ContentScale`: `CROP`, `FIT`, `FILL_HEIGHT`, `FILL_WIDTH`, `INSIDE`, `FILL_BOUNDS`

**Note:** Renders nothing if `resourceName` is not found in `DrawableResourcesHolder`. Register drawable resources via `mosaicDependencyInjectionConfig(drawableResources = mapOf(...))`.

**Supported triggers:** none

---

### AsyncImageTileSchema
**JSON type:** `"AsyncImage"`

| Field | Type | Default |
|---|---|---|
| `url` | `String` | required |
| `contentDescription` | `String?` | — |
| `contentScale` | `ContentScale` | — |
| `alpha` | `Float` | — |
| `clipToBounds` | `Boolean` | — |
| `alignment` | `AlignmentSchema.TwoDimensional` | — |

`ContentScale`: `CROP`, `FIT`, `FILL_HEIGHT`, `FILL_WIDTH`, `INSIDE`, `FILL_BOUNDS`

**Note:** Uses Coil 3's `AsyncImage`. All three load-state triggers are always wired regardless of events registration.

**Supported triggers:** `OnAsyncImageLoadStart`, `OnAsyncImageLoadSuccess`, `OnAsyncImageLoadFailure`

---

## Containers / Grouping

### ColumnTileSchema
**JSON type:** `"Column"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `arrangement` | `ArrangementSchema.Vertical` | — |
| `alignment` | `AlignmentSchema.Horizontal` | — |
| `scrollable` | `Boolean` | `false` |
| `filterChildrenByTerm` | `String?` | `null` |

**Note:** Exposes `LocalColumnScope` CompositionLocal for children that need `ColumnScope` modifiers (e.g. `weight`). Broadcast channel (shared with `LazyColumnTileSchema`): `ScrollToTop`, `ScrollTo(index)`, `ScrollToBottom`. When `filterChildrenByTerm` is non-null, only children whose `searchableTerms` contains the term (case-insensitive) are rendered; children with `null` `searchableTerms` are always shown.

**Supported triggers:** `OnClick`, `OnLongPress`, `OnScrolled`

---

### LazyColumnTileSchema
**JSON type:** `"LazyColumn"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required (items keyed by id) |
| `arrangement` | `ArrangementSchema.Vertical` | — |
| `alignment` | `AlignmentSchema.Horizontal` | — |
| `scrollThreshold` | `Int?` | `null` |
| `considerLoadingItemAtEndOnThresholdReached` | `Boolean` | `true` |
| `filterChildrenByTerm` | `String?` | `null` |

**Note:** Uses Compose `LazyColumn` — only visible items are composed. Items are keyed by tile id. Exposes `LocalLazyColumnRenderingScope` CompositionLocal (provides `LazyItemScope`). `scrollThreshold` fires `OnScrollThresholdReached` when the user scrolls within that many items of the end. Broadcast channel shared with `ColumnTileSchema`: `ScrollToTop`, `ScrollTo(index)`, `ScrollToBottom`. When `filterChildrenByTerm` is non-null, only children whose `searchableTerms` contains the term (case-insensitive) are rendered; children with `null` `searchableTerms` are always shown.

**Supported triggers:** `OnScrolled`, `OnScrollThresholdReached`, `OnClick`, `OnLongPress`

---

### RowTileSchema
**JSON type:** `"Row"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `arrangement` | `ArrangementSchema.Horizontal` | — |
| `alignment` | `AlignmentSchema.Vertical` | — |
| `scrollable` | `Boolean` | `false` |

**Note:** Exposes `LocalRowScope` CompositionLocal for children that need `RowScope` modifiers (e.g. `weight`). Broadcast channel (shared with `LazyRowTileSchema`): `ScrollToStart`, `ScrollTo(index)`, `ScrollToEnd`.

**Supported triggers:** `OnClick`, `OnLongPress`, `OnScrolled`

---

### LazyRowTileSchema
**JSON type:** `"LazyRow"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required (items keyed by id) |
| `arrangement` | `ArrangementSchema.Horizontal` | — |
| `alignment` | `AlignmentSchema.Vertical` | — |
| `scrollThreshold` | `Float?` | `null` |
| `considerLoadingItemAtEndOnThresholdReached` | `Boolean` | `false` |

**Note:** Uses Compose `LazyRow` — only visible items are composed. Items are keyed by tile id. Exposes `LocalLazyRowRenderingScope` CompositionLocal (provides `LazyItemScope`). `scrollThreshold` fires `OnScrollThresholdReached` with direction `End` or `Start`. Broadcast channel shared with `RowTileSchema`: `ScrollToStart`, `ScrollTo(index)`, `ScrollToEnd`.

**Supported triggers:** `OnScrolled`, `OnScrollThresholdReached`, `OnClick`, `OnLongPress`

---

### BoxTileSchema
**JSON type:** `"Box"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `alignment` | `AlignmentSchema.TwoDimensional` |

**Note:** Stacking container — children overlay on top of each other. No scroll. No scoped CompositionLocal.

**Supported triggers:** `OnClick`, `OnLongPress`

---

### CardTileSchema
**JSON type:** `"Card"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `kind` | `Kind` |

`Kind`: `DEFAULT` (filled), `ELEVATED` (shadow), `OUTLINED` (border)

**Note:** Uses `ColumnScope` internally. Exposes `LocalColumnScope`. Always fires `OnClick` regardless of events registration.

**Supported triggers:** `OnClick`

---

### GridTileSchema
**JSON type:** `"Grid"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `columns` | `List<GridTrackSchema>` | required |
| `rows` | `List<GridTrackSchema>` | required |
| `columnGap` | `Int` | `0` |
| `rowGap` | `Int` | `0` |
| `flow` | `GridFlowSchema` | — |

`GridTrackSchema` (sealed):
- `Fixed(value: Int)` — fixed dp size
- `Fraction(value: Float)` — fraction of available space (0.0–1.0)
- `Flexible(value: Float)` — flex units (`fr`)
- `Auto` — size to content
- `MaxContent` — max content size
- `MinContent` — min content size

`GridFlowSchema`: `Row` (row-first), `Column` (column-first)

**Note:** Uses Compose experimental Grid API (`@OptIn(ExperimentalGridApi)`). Eager composition (not lazy). Exposes `LocalGridScope` CompositionLocal for children needing grid placement modifiers (columnSpan, rowSpan).

**Supported triggers:** `OnClick`

---

### FlexBoxTileSchema
**JSON type:** `"FlexBox"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `direction` | `FlexDirectionSchema` | — |
| `justifyContent` | `JustifyContentSchema` | — |
| `alignItems` | `AlignItemsSchema` | — |
| `alignContent` | `AlignContentSchema` | — |
| `wrap` | `WrapSchema` | — |
| `columnGap` | `Int` | `0` |
| `rowGap` | `Int` | `0` |

`FlexDirectionSchema`: `Row`, `RowReverse`, `Column`, `ColumnReverse`
`JustifyContentSchema`: `Start`, `Center`, `End`, `SpaceBetween`, `SpaceAround`, `SpaceEvenly`
`AlignItemsSchema`: `Start`, `End`, `Center`, `Stretch`, `Baseline`
`AlignContentSchema`: `Start`, `End`, `Center`, `Stretch`, `SpaceBetween`, `SpaceAround`
`WrapSchema`: `NoWrap`, `Wrap`, `WrapReverse`

**Note:** Uses Compose experimental FlexBox API (`@OptIn(ExperimentalFlexBoxApi)`). Exposes `LocalFlexBoxScope` CompositionLocal for children needing flex modifiers (flexGrow, flexShrink, alignSelf).

**Supported triggers:** `OnClick`

---

### FlowRowTileSchema
**JSON type:** `"FlowRow"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `horizontalArrangement` | `ArrangementSchema.Horizontal` | — |
| `verticalArrangement` | `ArrangementSchema.Vertical` | — |
| `maxItemsInEachRow` | `Int?` | `null` (unlimited) |

**Note:** Uses Compose `FlowRow` — items wrap to the next row when the row is full. Eager composition (not lazy). Exposes `LocalFlowRowScope` CompositionLocal for children.

**Supported triggers:** `OnClick`

---

### PagerTileSchema
**JSON type:** `"Pager"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `pageSize` | `PageSizeSchema` | `Fill` |
| `pageSpacing` | `Int` | `0` |
| `contentPadding` | `Int` | `0` |
| `beyondViewportPageCount` | `Int` | `0` |

`PageSizeSchema` (sealed): `Fill` (fills viewport), `Fixed(value: Int dp)`

**Note:** Uses Compose `HorizontalPager`. Page count derived from `tiles.size`. Initial page change from page 0 is suppressed to avoid spurious triggers on composition. Broadcast: `ScrollToBegin`, `ScrollToEnd`, `ScrollToNextPage`, `ScrollToPreviousPage`.

`OnPageChanged` triggers fired per change: `Direction.Any` (always), `Direction.Start` (if new page is 0), `Direction.End` (if new page is last), `Direction.Index(n)` (always, carrying zero-based page index).

**Supported triggers:** `OnPageChanged`

---

### CarouselTileSchema
**JSON type:** `"Carousel"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `type` | `CarouselType` | — |
| `itemSpacing` | `Int` | — |
| `contentPadding` | `Int` | — |
| `userScrollEnabled` | `Boolean` | `true` |

`CarouselType`: `MultiBrowse` (multiple items with peeking), `Uncontained` (fixed width items)

**Note:** Material 3 horizontal carousel. `contentPadding` applied as symmetric horizontal padding. `userScrollEnabled = false` for programmatic-only scrolling.

**Supported triggers:** none

---

### ShimmerTileSchema
**JSON type:** `"Shimmer"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |

**Note:** Always active while visible. To stop shimmer: replace or hide the tile. Uses `compose-shimmer` library.

**Supported triggers:** none

---

### PullToRefreshTileSchema
**JSON type:** `"PullToRefresh"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `isRefreshing` | `Boolean` |

**Note:** Uses Material 3 `PullToRefreshBox`. Server must toggle `isRefreshing = false` via `UpdateTiles` when refresh completes. `style`/`visibility` are not forwarded to `PullToRefreshBox`.

**Supported triggers:** `OnPull`

---

### AdaptiveVisibilityTileSchema
**JSON type:** `"AdaptiveVisibility"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `width_visibility` | `WidthVisibility` | required (DSL default: `widthVisibleUntilExtraLarge()`) |
| `height_visibility` | `HeightVisibility` | required (DSL default: `heightVisibleUntilExpanded()`) |

`WidthVisibility` / `HeightVisibility`: `{ "type": "visible_from" | "visible_until", "breakpoint": { "type": ... } }`

Semantics:
- `visible_from` — **exclusive**: visible at breakpoints **above** the specified one. `fromCompact` = Medium and up; `fromMedium` = Expanded and up.
- `visible_until` — **inclusive**: visible at the specified breakpoint **and below**. `untilMedium` = Compact and Medium.

`WidthBreakpoint`: `compact`, `medium`, `expanded`, `large`, `extra_large`.
`HeightBreakpoint`: `compact`, `medium`, `expanded`.

**Note:** Transparent (logical) container. Observes `currentWindowAdaptiveInfoV2()` and only composes children when **both** axes are satisfied. Creates no layout node — `style` is not applied.

DSL helpers (in `AdaptiveVisibilityTileSchemaBuilder.kt`): `widthVisibleFrom[Compact|Medium|Expanded|Large|ExtraLarge]()`, `widthVisibleUntil[...]()`, `heightVisibleFrom[Compact|Medium|Expanded]()`, `heightVisibleUntil[...]()`.

**Supported triggers:** `OnWidthBreakpointSatisfied`, `OnWidthBreakpointNotSatisfied`, `OnHeightBreakpointSatisfied`, `OnHeightBreakpointNotSatisfied`

---

### LazyTilesTileSchema
**JSON type:** `"LazyTiles"`

Self-loading tile container that fetches child tiles from a remote endpoint on first render.

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>?` | `null` (loaded remotely on first render) |
| `placeholderTiles` | `List<TileSchema>` | shown while loading |
| `failureTiles` | `List<TileSchema>` | shown on error |
| `isFailureState` | `Boolean` | `false` |
| `url` | `String` | required |
| `method` | `HttpMethod` | required |
| `body` | `AnySerializable?` | — |
| `headers` | `Map<String, String>?` | — |

`HttpMethod`: `GET`, `POST`, `PUT`, `DELETE`, `PATCH`

**Note:** Network call via `SingleEffect` (one-shot). If `tiles` is non-null, fetch is skipped. Response decoded as `List<TileSchema>`. Outer layout is `Column`. Use `ReloadLazyTilesEventSchema` to force re-fetch.

**Supported triggers:** `OnLoadTilesStart`, `OnLoadTilesSuccess`, `OnLoadTilesFailure`

---

## App Bars

### TopAppBarTileSchema
**JSON type:** `"TopAppBar"`

| Field | Type | Default |
|---|---|---|
| `title` | `TileSchema` | required |
| `navigationIcon` | `TileSchema?` | `null` |
| `actions` | `List<TileSchema>?` | `null` |
| `barStyle` | `TopAppBarStyle` | `DEFAULT` |

`TopAppBarStyle`: `DEFAULT` (small, title left-aligned), `CENTER_ALIGNED` (title centered), `MEDIUM` (collapsible, medium height), `LARGE` (collapsible, large height)

**Note:** Slots rendered as independent `RenderChild` calls (not in `RenderChildren` hierarchy). Triggers come from child tiles, not the app bar itself.

**Supported triggers:** none

---

### BottomAppBarTileSchema
**JSON type:** `"BottomAppBar"`

| Field | Type | Default |
|---|---|---|
| `actions` | `List<TileSchema>` | required |
| `floatingActionButton` | `TileSchema?` | `null` |

**Note:** `actions` rendered in `RowScope`. `floatingActionButton` rendered independently. Triggers come from child tiles.

**Supported triggers:** none

---

## Navigation

### TabsTileSchema
**JSON type:** `"Tabs"`

| Field | Type |
|---|---|
| `selectedTabId` | `String` |
| `tabItems` | `List<TabItem>` |
| `tabType` | `Type` |
| `scrollable` | `Boolean` |

`TabItem`: `{ id: String, label: String?, icon: IconSchema?, badgeText: String? }`
`Type`: `PRIMARY`, `SECONDARY`

**Note:** Selected index computed client-side from `selectedTabId`. `badgeText` field is defined in schema but not yet rendered in the current renderer.

**Supported triggers:** `OnTabItemClick`

---

### NavigationBarTileSchema
**JSON type:** `"NavigationBar"`

| Field | Type |
|---|---|
| `items` | `List<NavigationBarItem>` |
| `selectedItemId` | `String` |

`NavigationBarItem`: `{ id: String, icon: IconSchema, label: String? }`

**Note:** Selected state is server-driven. Icon filling (filled vs outlined) determined by id matching `selectedItemId`.

**Supported triggers:** `OnNavigationBarItemClick`

---

### NavigationRailTileSchema
**JSON type:** `"NavigationRail"`

| Field | Type | Default |
|---|---|---|
| `items` | `List<NavigationRailItem>` | required |
| `selectedItemId` | `String` | required |
| `header` | `TileSchema?` | `null` |
| `footer` | `TileSchema?` | `null` |

`NavigationRailItem`: `{ id: String, icon: IconSchema, label: String? }`

**Note:** `header` rendered at top; `footer` pushed to bottom via `weight(1f)` Spacer. Fixed padding applied (4.dp horizontal, 8.dp vertical).

**Supported triggers:** `OnNavigationRailItemClick`

---

### NestedNavigationGraphTileSchema
**JSON type:** `"NestedNavigationGraph"`

| Field | Type | Default |
|---|---|---|
| `entries` | `List<Entry>` | required |
| `startEntryId` | `String` | required |
| `defaultTransition` | `TransitionSchema?` | `null` |
| `defaultPopTransition` | `TransitionSchema?` | `null` |
| `defaultPredictivePopTransition` | `TransitionSchema?` | `null` |

`Entry`: `{ screenId: String, initialTiles: List<TileSchema>, initialEvents: List<EventSchema>?, failureTiles: List<TileSchema>?, failureEvents: List<EventSchema>?, transition: TransitionSchema?, popTransition: TransitionSchema?, predictivePopTransition: TransitionSchema? }`

**Note:** Embeds a Navigation 3 `NavDisplay` hosting a full navigation back-stack. Registers a `NavigationController` in `NavigatorsHolder` (unregistered on `DisposableEffect`). Per-entry transitions override defaults. Back-stack key type is `ScreenNavKey`. ViewModel scope preserved per entry.

**Supported triggers:** `OnNavigationEntrySet`, `OnNavigationEntryChanged`

---

## Progress Indicators

### CircularProgressIndicatorTileSchema
**JSON type:** `"CircularProgressIndicator"`

| Field | Type | Default |
|---|---|---|
| `progress` | `Float?` | `null` (indeterminate) |

**Note:** `progress` non-null = determinate mode with fixed fill; `progress` null = animated indeterminate spinner.

**Supported triggers:** none

---

### LinearProgressIndicatorTileSchema
**JSON type:** `"LinearProgressIndicator"`

| Field | Type | Default |
|---|---|---|
| `progress` | `Float?` | `null` (indeterminate) |

**Note:** Same determinate/indeterminate toggle as `CircularProgressIndicatorTileSchema`.

**Supported triggers:** none

---

## Badge

### BadgeTileSchema
**JSON type:** `"Badge"`

| Field | Type | Default |
|---|---|---|
| `content` | `String?` | `null` (renders as plain dot) |

**Note:** `content` non-null = text in pill; `content` null = plain dot. Typically used inside `BadgedBox`.

**Supported triggers:** none

---

## Search

### SearchBarTileSchema
**JSON type:** `"SearchBar"`

| Field | Type | Default |
|---|---|---|
| `query` | `String` | `""` |
| `placeholder` | `String?` | `null` |
| `leadingIcon` | `TileSchema?` | `null` |

**Note:** Built on `BasicTextField` with Material 3 styling. Fixed height 56 dp. IME action `ImeAction.Search`. Trailing icon is a hardcoded clear button (appears animated when `query` is non-empty) — not configurable from schema. Server must update `query` via `UpdateTiles` on each keystroke. Custom triggers: `onQueryCleared` (clear button), `onSearch` (IME search action).

**Supported triggers:** `OnTextChanged`, `OnQueryChanged`, `OnQueryCleared`

---

## Menu

### MenuTileSchema
**JSON type:** `"Menu"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `items` | `List<MenuItem>` |
| `expanded` | `Boolean` |

`MenuItem`: `{ id: String, label: String, leadingIcon: IconSchema?, trailingIcon: IconSchema? }`

**Note:** `Box` overlaying a `DropdownMenu` on top of anchor content (`tiles`). `expanded` state is server-driven. When user dismisses menu externally, renderer dispatches `MenuTileEvents.OnToggleMenu` → `MenuTileHolder` toggles `tile.expanded`. Max dropdown height 400 dp. Complex builder scenario — `MenuTileSchemaBuilder` has nested `MenuItem` builders; always study the existing implementation before modifying.

**Supported triggers:** `OnMenuItemClick`
