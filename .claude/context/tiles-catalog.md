# Mosaic — Tiles Catalog

All tiles implement `TileSchema`. Every tile inherits these base fields:

| Field | Type | Description |
|---|---|---|
| `id` | `String` | Unique identifier within the screen |
| `events` | `List<EventSchema>?` | Events attached to this tile |
| `style` | `StyleSchema` | Size, margin, padding, background, border, clip, windowInsets |
| `visibility` | `TileSchema.Visibility` | `VISIBLE`, `INVISIBLE` (takes space), `GONE` (no space) |

JSON serialization uses `@SerialName` as the `type` discriminator field.

---

## Text

### SimpleTextTileSchema
**JSON type:** `"Text"`

| Field | Type | Default |
|---|---|---|
| `text` | `String` | required |
| `color` | `ColorSchema` | required |
| `typography` | `TypographySchema` | required |

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

`Size`: `DEFAULT`, `MEDIUM`, `LARGE`

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

**Supported triggers:** `OnTextChanged`, `OnKeyboardDone`, `OnKeyboardGo`, `OnKeyboardNext`, `OnKeyboardPrevious`, `OnKeyboardSearch`, `OnKeyboardSend`

---

### CheckboxTileSchema
**JSON type:** `"Checkbox"`

| Field | Type |
|---|---|
| `checked` | `Boolean` |
| `enabled` | `Boolean` |

**Supported triggers:** `OnCheck`, `OnUncheck`, `OnCheckChanged`

---

### RadioButtonTileSchema
**JSON type:** `"RadioButton"`

| Field | Type |
|---|---|
| `selected` | `Boolean` |
| `enabled` | `Boolean` |
| `groupId` | `String` |

**Supported triggers:** `OnSelect`

---

### SwitchTileSchema
**JSON type:** `"Switch"`

| Field | Type |
|---|---|
| `checked` | `Boolean` |
| `enabled` | `Boolean` |

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

**Supported triggers:** `OnAsyncImageLoadStart`, `OnAsyncImageLoadSuccess`, `OnAsyncImageLoadFailure`

---

## Containers / Grouping

### AdaptiveVisibilityTileSchema
**JSON type:** `"AdaptiveVisibility"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `width_visibility` | `WidthVisibility` | required (DSL default: `widthVisibleUntilExtraLarge()`) |
| `height_visibility` | `HeightVisibility` | required (DSL default: `heightVisibleUntilExpanded()`) |

`WidthVisibility` / `HeightVisibility`: `{ "type": "visible_from" | "visible_until", "breakpoint": { "type": ... } }`

Semântica dos modos:
- `visible_from` — **exclusivo**: visível nos breakpoints **acima** do especificado. `fromCompact` = Medium em diante; `fromMedium` = Expanded em diante.
- `visible_until` — **inclusivo**: visível no breakpoint especificado **e abaixo**. `untilMedium` = Compact e Medium.

`WidthBreakpoint`: `compact`, `medium`, `expanded`, `large`, `extra_large`. `HeightBreakpoint`: `compact`, `medium`, `expanded`.

Transparent (logical) container: observes `currentWindowAdaptiveInfoV2()` and only composes its children when **both** axes are satisfied. Creates no layout node of its own — `style` is not applied.

DSL helpers (in `AdaptiveVisibilityTileSchemaBuilder.kt`): `widthVisibleFrom[Compact|Medium|Expanded|Large|ExtraLarge]()`, `widthVisibleUntil[...]()`, `heightVisibleFrom[Compact|Medium|Expanded]()`, `heightVisibleUntil[...]()`.

**Supported triggers:** `OnWidthBreakpointSatisfied`, `OnWidthBreakpointNotSatisfied`, `OnHeightBreakpointSatisfied`, `OnHeightBreakpointNotSatisfied`

---

### ColumnTileSchema
**JSON type:** `"Column"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `arrangement` | `ArrangementSchema.Vertical` | — |
| `alignment` | `AlignmentSchema.Horizontal` | — |
| `isScrollable` | `Boolean` | — |
| `lazyRender` | `Boolean` | — |

**Supported triggers:** `OnClick`, `OnLongPress`

---

### RowTileSchema
**JSON type:** `"Row"`

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `arrangement` | `ArrangementSchema.Horizontal` | — |
| `alignment` | `AlignmentSchema.Vertical` | — |
| `isScrollable` | `Boolean` | — |
| `lazyRender` | `Boolean` | — |

**Supported triggers:** `OnClick`, `OnLongPress`

---

### BoxTileSchema
**JSON type:** `"Box"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `alignment` | `AlignmentSchema.TwoDimensional` |

**Supported triggers:** `OnClick`, `OnLongPress`

---

### CardTileSchema
**JSON type:** `"Card"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `kind` | `Kind` |

`Kind`: `DEFAULT`, `ELEVATED`, `OUTLINED`

**Supported triggers:** none

---

### GridTileSchema
**JSON type:** `"Grid"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `columns` | `Int` |
| `gutter` | `Int` |

**Supported triggers:** none

---

### PagerTileSchema
**JSON type:** `"Pager"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `columns` | `Int` |
| `gutter` | `Int` |
| `contentHorizontalPadding` | `Int` |

**Supported triggers:** none (scroll triggers planned)

---

### CarouselTileSchema
**JSON type:** `"Carousel"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `contentHorizontalPadding` | `Int` |
| `columns` | `Int` |
| `gutter` | `Int` |

**Supported triggers:** none

---

### ShimmerTileSchema
**JSON type:** `"Shimmer"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |

**Supported triggers:** none

---

### PullToRefreshTileSchema
**JSON type:** `"PullToRefresh"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `isRefreshing` | `Boolean` |

**Supported triggers:** `OnPull`

---

### LazyTilesTileSchema
**JSON type:** `"LazyTiles"`

Fetches its tile list from a remote URL on first render.

| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>?` | `null` (loaded remotely) |
| `placeholderTiles` | `List<TileSchema>` | shown while loading |
| `failureTiles` | `List<TileSchema>` | shown on error |
| `isFailureState` | `Boolean` | `false` |
| `url` | `String` | required |
| `method` | `HttpMethod` | required |
| `body` | `AnySerializable?` | — |
| `headers` | `Map<String, String>?` | — |

**Supported triggers:** none

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

`TopAppBarStyle`: `DEFAULT`, `CENTER_ALIGNED`, `MEDIUM`, `LARGE`

**Supported triggers:** none

---

### BottomAppBarTileSchema
**JSON type:** `"BottomAppBar"`

| Field | Type | Default |
|---|---|---|
| `actions` | `List<TileSchema>` | required |
| `floatingActionButton` | `TileSchema?` | `null` |

**Supported triggers:** none

---

## Navigation

### NavigationBarTileSchema
**JSON type:** `"NavigationBar"`

| Field | Type |
|---|---|
| `items` | `List<NavigationBarItem>` |
| `selectedItemId` | `String` |

`NavigationBarItem`: `{ id: String, icon: IconSchema, label: String? }`

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

**Supported triggers:** `OnNavigationRailItemClick`

---

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

**Supported triggers:** `OnTabItemClick`

---

### NestedNavigationGraphTileSchema
**JSON type:** `"NestedNavigationGraph"`

| Field | Type |
|---|---|
| `navigatorId` | `String` |
| `entries` | `List<Entry>` |
| `startEntryId` | `String` |

`Entry`: `{ screenId: String, initialTiles: List<TileSchema>, initialEvents: List<EventSchema>, failureTiles: List<TileSchema>, failureEvents: List<EventSchema> }`

**Supported triggers:** `OnNavigationEntryChanged`, `OnNavigationEntrySet`

---

## Progress Indicators

### CircularProgressIndicatorTileSchema
**JSON type:** `"CircularProgressIndicator"`

| Field | Type | Default |
|---|---|---|
| `progress` | `Float?` | `null` (indeterminate) |

**Supported triggers:** none

---

### LinearProgressIndicatorTileSchema
**JSON type:** `"LinearProgressIndicator"`

| Field | Type | Default |
|---|---|---|
| `progress` | `Float?` | `null` (indeterminate) |

**Supported triggers:** none

---

## Miscellaneous

### BadgeTileSchema
**JSON type:** `"Badge"`

| Field | Type | Default |
|---|---|---|
| `content` | `String?` | `null` (dot only) |

**Supported triggers:** none

---

### SuggestionChipTileSchema
**JSON type:** `"SuggestionChip"`

| Field | Type | Default |
|---|---|---|
| `text` | `String` | required |
| `icon` | `IconSchema?` | `null` |
| `enabled` | `Boolean` | required |
| `variant` | `Variant` | `DEFAULT` |

`Variant`: `DEFAULT` (flat, with border → `SuggestionChip`), `ELEVATED` (shadow, no border → `ElevatedSuggestionChip`)

**Supported triggers:** `OnClick`

---

### AssistChipTileSchema
**JSON type:** `"AssistChip"`

| Field | Type | Default |
|---|---|---|
| `text` | `String` | required |
| `leadingIcon` | `IconSchema?` | `null` |
| `trailingIcon` | `IconSchema?` | `null` |
| `enabled` | `Boolean` | required |
| `variant` | `Variant` | `DEFAULT` |

`Variant`: `DEFAULT` (flat, with border → `AssistChip`), `ELEVATED` (shadow, no border → `ElevatedAssistChip`)

**Use case:** action chip with optional icons on both ends. Suitable as a menu trigger (e.g. leading icon + label + chevron as trailing icon, full chip click opens menu).

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

`Variant`: `DEFAULT` (flat → `FilterChip`), `ELEVATED` (shadow → `ElevatedFilterChip`)

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

**Note:** No `variant` field — `InputChip` has no elevated variant in Material 3.

**Note:** `trailingIcon` is purely visual. The `InputChip` composable exposes only a single `onClick` for the whole chip surface; there is no separate trailing-icon click callback.

**Supported triggers:** `OnCheck`, `OnUncheck`, `OnCheckChanged`

---

### SearchBarTileSchema
**JSON type:** `"SearchBar"`

| Field | Type | Default |
|---|---|---|
| `query` | `String` | `""` |
| `placeholder` | `String?` | `null` |
| `leadingIcon` | `TileSchema?` | `null` |
| `trailingIcon` | `TileSchema?` | `null` |

**Supported triggers:** `OnTextChanged`

---

### MenuTileSchema
**JSON type:** `"Menu"`

| Field | Type |
|---|---|
| `tiles` | `List<TileSchema>` |
| `items` | `List<MenuItem>` |
| `expanded` | `Boolean` |

`MenuItem`: `{ id: String, label: String, leadingIcon: IconSchema?, trailingIcon: IconSchema? }`

**Note:** This is a complex builder scenario. `MenuTileSchemaBuilder` has nested `MenuItem` builders. Always study the existing implementation before modifying.

**Supported triggers:** none (menu item clicks are handled by `ToggleMenuEventSchema`)
