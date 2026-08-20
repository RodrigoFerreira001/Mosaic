# Mosaic — Tile Catalog

Complete, field-by-field reference for every `TileSchema` shipped in `mosaic-core`. Every entry below was written directly from that schema's KDoc (the primary source of truth) and cross-checked against its `mosaic-server` DSL builder — not paraphrased from any other document. If the framework's actual behavior ever needs re-verifying, re-read the schema in `mosaic-core/.../data/schemas/tile/tiles/` and the matching builder in `mosaic-server/.../builder/tile/builders/`.

Every tile shares 5 base fields, always available and not repeated per entry below:

| Field | Type | Default | What it's for |
|---|---|---|---|
| `id` | `String` | random id | Unique identifier — how events, updates and lookups address this tile. |
| `events` | `List<EventSchema>?` | `null` | The events this tile can fire, each declaring which `EventTrigger` runs it. See `architecture.md` §5, "Event chaining". |
| `style` | `StyleSchema` | varies per tile | Size, margin, padding, background, border, clip, window insets — see `architecture.md` §5 for the fixed application order. |
| `visibility` | `TileSchema.Visibility` | `visible()` | `visible()` shows the tile; `invisible()` hides it but keeps its layout space; `gone()` removes it from layout entirely. |
| `searchableTerms` | `List<String>?` | `null` | Terms an ancestor container's `filterChildrenByTerm` matches against (case-insensitive substring) to decide whether this tile survives a filter. |

For the underlying mechanics of trigger matching, `TileGroupEvent`, how stateful tiles combine a local `TileEvent` with a remote `EventTrigger` on the same interaction, and `Modifier.size()`/`LocalXScope` availability, see [`architecture.md`](architecture.md) — this catalog documents *what* each tile does and *what its triggers are*, not how the dispatch machinery works.

---

## Text

### `SimpleText`

Renders plain text. `typography` provides the base text style (falls back to the ambient default when `null`); every other styling field overrides one property on top of that base and is ignored when `null`. Not clickable and dispatches no triggers — `events` declared on it are never fired. Renders plain text only, no inline annotations/links/markdown; wrap in a `SelectionContainer` to make it selectable.

**Parameters** (beyond the 5 base fields):

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `text` | `String` | required | Text content displayed. |
| `color` | `ColorSchema?` | `null` | Text color override. |
| `typography` | `TypographySchema?` | `null` | Base text style (size/weight/line-height as a set). Falls back to the ambient default. |
| `autoSize` | `AutoSizeSchema?` | `null` | Automatic font-size scaling to fit the available space. |
| `fontSize` | `Float?` | `null` | Font size override, in sp. |
| `fontStyle` | `FontStyleSchema?` | `null` | Font style override (e.g. italic). |
| `fontWeight` | `FontWeightSchema?` | `null` | Font weight override. |
| `fontFamily` | `FontFamilySchema?` | `null` | Font family override. |
| `letterSpacing` | `Float?` | `null` | Letter spacing override, in sp. |
| `textDecoration` | `TextDecorationSchema?` | `null` | Text decoration override (e.g. underline). |
| `textAlign` | `TextAlignSchema?` | `null` | Text alignment override. |
| `lineHeight` | `Float?` | `null` | Line height override, in sp. |
| `overflow` | `TextOverflowSchema?` | `null` (clip) | How overflowing text is handled. |
| `softWrap` | `Boolean?` | `null` (true) | Whether the text wraps at soft line breaks. |
| `maxLines` | `Int?` | `null` (unbounded) | Maximum number of lines shown. |
| `minLines` | `Int?` | `null` (1) | Minimum number of lines reserved. |

**DSL example:**
```kotlin
SimpleText(
    text = "Welcome back",
    typography = headlineSmallTypography(),
    color = color(ColorSchema.Theme.Color.ON_SURFACE)
)
```

**Triggers fired:** none.

**Notes:** `style` defaults to wrap-content on both axes (not the framework-wide fill/wrap default) so a bare `SimpleText("Hello")` doesn't stretch to its parent's width. `text` is the DSL function's first parameter (ahead of `id`/`events`/`style`), so the minimal call is just `SimpleText("Hello")`.

## Buttons

### `Button`

Renders a Material 3 button. The concrete composable is picked by `buttonType`: `FILLED` → `Button`, `ELEVATED` → `ElevatedButton`, `FILLED_TONAL` → `FilledTonalButton`, `OUTLINED` → `OutlinedButton`, `TEXT` → `TextButton`. `shape` picks the corner style: `SQUARE` → theme's medium corner radius, `ROUNDED` → fully rounded (pill/circle).

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `text` | `String` | required | Label displayed on the button. |
| `icon` | `IconSchema?` | `null` | Optional icon rendered alongside the text. |
| `buttonType` | `Type` (`FILLED`/`ELEVATED`/`FILLED_TONAL`/`OUTLINED`/`TEXT`) | `FILLED` | Visual variant — DSL helpers `filledButton()`, `elevatedButton()`, `filledTonalButton()`, `outlinedButton()`, `textButton()`. |
| `shape` | `Shape` (`SQUARE`/`ROUNDED`) | `ROUNDED` | Corner style — `squareButton()`/`roundedButton()`. |
| `loading` | `Boolean` | `false` | Shows a 24dp spinner instead of the content, and forces the button disabled for real (see Notes). |
| `enabled` | `Boolean` | `true` | Whether the button is interactive. Ignored (treated as disabled) while `loading` is `true`. |
| `iconPosition` | `IconPosition` (`START`/`END`) | `START` | Where the icon sits relative to the text — `iconAtStart()`/`iconAtEnd()`, separated by an 8dp spacer. |

**DSL example:**
```kotlin
Button(
    id = "submit",
    text = "Sign in",
    buttonType = filledButton(),
    events = {
        SendNetworkRequest(
            trigger = EventTriggers.onClick(),
            url = "/api/login",
            method = HttpMethod.POST
        )
    }
)
```

**Triggers fired:** `OnClick` — fired when tapped while interactive.

**Notes:** the button is interactive only when `enabled` is `true` **and** `loading` is `false` — the two combine into the single `enabled` the Material composable receives, so a loading button takes Material's real disabled colors and is reported as disabled to accessibility services, not just visually different. A slow action can't be submitted twice by mashing the button.

### `FloatingActionButton`

Renders a Material 3 floating action button. `size` picks both the composable and the icon's rendered size: `DEFAULT` (small) → `SmallFloatingActionButton`, icon scaled to 24dp; `MEDIUM` → `FloatingActionButton`, 28dp; `LARGE` → `LargeFloatingActionButton`, 36dp.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `icon` | `IconSchema` | required | Icon rendered inside the FAB; its `size` field is overridden based on `size` (24/28/36dp) regardless of what's passed in. |
| `size` | `Size` (`DEFAULT`/`MEDIUM`/`LARGE`) | `defaultFloatingActionButon()` | FAB size — DSL helpers `defaultFloatingActionButon()`, `mediumFloatingActionButon()`, `largeFloatingActionButon()`. |

**DSL example:**
```kotlin
FloatingActionButton(
    id = "add",
    icon = icon("add"),
    size = mediumFloatingActionButon(),
    events = { AddTiles(trigger = EventTriggers.onClick(), groupingTileId = "list", tiles = { /* ... */ }) }
)
```

**Triggers fired:** `OnClick` — fired when tapped.

**Notes:** no `enabled`/`loading` — Material 3 FAB composables intentionally have no `enabled` parameter, since a disabled-but-visible FAB fights the emphasis it's meant to carry. Hide it via the inherited `visibility` field (`"GONE"`/`"VISIBLE"` via `UpdateTiles`) when its action isn't currently available, instead of trying to gray it out. `style` defaults to wrap-content on both axes.

### `IconButton`

Renders a Material 3 icon-only button. The concrete composable is picked by `buttonType`: `DEFAULT` → `IconButton`, `FILLED` → `FilledIconButton`, `FILLED_TONAL` → `FilledTonalIconButton`, `OUTLINED` → `OutlinedIconButton`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `icon` | `IconSchema` | required | Icon rendered inside the button, with its color/size/style applied as-is. |
| `buttonType` | `Type` (`DEFAULT`/`FILLED`/`FILLED_TONAL`/`OUTLINED`) | `defaultIconButton()` | Visual variant — DSL helpers `defaultIconButton()`, `filledIconButton()`, `filledTonalIconButton()`, `outlinedIconButton()`. |
| `loading` | `Boolean` | `false` | Shows a 24dp spinner instead of the icon, and forces the button disabled for real. |
| `enabled` | `Boolean` | required (no default) | Whether the button is interactive. Ignored (treated as disabled) while `loading` is `true`. |

**DSL example:**
```kotlin
IconButton(
    id = "favorite",
    icon = icon("favorite"),
    buttonType = filledTonalIconButton(),
    enabled = true,
    events = { ToggleMenu(trigger = EventTriggers.onClick(), menuId = "options") }
)
```

**Triggers fired:** `OnClick` — fired when tapped while interactive.

**Notes:** unlike the FAB, `IconButton`'s underlying Material composables do accept `enabled` natively, so it's forwarded directly rather than simulated. `style` defaults to wrap-content on both axes. `enabled` has no default — must always be passed explicitly.

## Chips

### `AssistChip`

Renders a Material 3 assist chip — suggests a single contextual action. `variant` picks the composable: `DEFAULT` → `AssistChip` (outlined), `ELEVATED` → `ElevatedAssistChip`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `text` | `String` | required | Label displayed on the chip. |
| `leadingIcon` | `IconSchema?` | `null` | Optional icon before the text. |
| `trailingIcon` | `IconSchema?` | `null` | Optional icon after the text. |
| `enabled` | `Boolean` | required (no default) | Forwarded directly to the underlying Material composable. |
| `variant` | `Variant` (`DEFAULT`/`ELEVATED`) | `defaultAssistChip()` | Visual variant — `defaultAssistChip()`/`elevatedAssistChip()`. |

**DSL example:**
```kotlin
AssistChip(
    id = "call",
    text = "Call support",
    leadingIcon = icon("call"),
    enabled = true,
    events = { OpenExternalLink(trigger = EventTriggers.onClick(), url = "tel:+15551234567") }
)
```

**Triggers fired:** `OnClick` — fired when tapped.

**Notes:** stateless — no selected state. Both icons are purely decorative: tapping either one fires the same chip-level click as tapping anywhere else on the chip.

### `FilterChip`

Renders a Material 3 filter chip with a toggleable `selected` state — used to filter a list or result set. `variant` picks the composable: `DEFAULT` → `FilterChip` (outlined), `ELEVATED` → `ElevatedFilterChip`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `text` | `String` | required | Label displayed on the chip. |
| `selected` | `Boolean` | required (no default) | Current selection state. |
| `leadingIcon` | `IconSchema?` | `null` | Optional icon before the text. |
| `trailingIcon` | `IconSchema?` | `null` | Optional icon after the text. |
| `enabled` | `Boolean` | required (no default) | Forwarded directly to the underlying Material composable. |
| `variant` | `Variant` (`DEFAULT`/`ELEVATED`) | `defaultFilterChip()` | Visual variant — `defaultFilterChip()`/`elevatedFilterChip()`. |

**DSL example:**
```kotlin
FilterChip(
    id = "filter_active",
    text = "Active",
    selected = true,
    enabled = true,
    events = { UpdateTiles(trigger = EventTriggers.onCheckChanged(), updates = { /* refresh list */ }) }
)
```

**Triggers fired (in this order, on every tap):** `OnCheck` (becomes selected) or `OnUncheck` (becomes unselected), then always `OnCheckChanged`.

**Notes:** tapping the chip flips `selected` locally first (no round trip needed for the visual state to update) — the three triggers above fire in parallel so the server-authored `events` can react. The holder exposes the current `selected` boolean by this tile's `id`, so `GetData`/`EvaluateData` can read it via the `Tile` data source.

### `InputChip`

Renders a Material 3 `InputChip` with a toggleable `selected` state — represents a piece of user input (e.g. a selected tag). Always maps to the flat `InputChip` composable; unlike the other three chips, it has no `variant`/elevated option.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `text` | `String` | required | Label displayed on the chip. |
| `selected` | `Boolean` | required (no default) | Current selection state. |
| `leadingIcon` | `IconSchema?` | `null` | Optional icon before the text. |
| `trailingIcon` | `IconSchema?` | `null` | Optional icon after the text — decorative, see Notes. |
| `enabled` | `Boolean` | required (no default) | Forwarded directly to the underlying Material composable. |

**DSL example:**
```kotlin
InputChip(
    id = "tag_kotlin",
    text = "Kotlin",
    selected = true,
    enabled = true
)
```

**Triggers fired (in this order, on every tap):** `OnCheck`/`OnUncheck`, then always `OnCheckChanged` — identical sequence to `FilterChip`.

**Notes:** same local-selection-first mechanism as `FilterChip`; the holder exposes `selected` by `id` for `GetData`/`EvaluateData`. `trailingIcon` does **not** fire a separate dismiss trigger despite what its position suggests — tapping anywhere on the chip toggles it the same way.

### `SuggestionChip`

Renders a Material 3 suggestion chip — surfaces a dynamically generated suggestion. `variant` picks the composable: `DEFAULT` → `SuggestionChip` (outlined), `ELEVATED` → `ElevatedSuggestionChip`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `text` | `String` | required | Label displayed on the chip. |
| `icon` | `IconSchema?` | `null` | Optional leading icon. |
| `enabled` | `Boolean` | required (no default) | Forwarded directly to the underlying Material composable. |
| `variant` | `Variant` (`DEFAULT`/`ELEVATED`) | `defaultSuggestionChip()` | Visual variant — `defaultSuggestionChip()`/`elevatedSuggestionChip()`. |

**DSL example:**
```kotlin
SuggestionChip(
    id = "suggest_1",
    text = "Try dark mode",
    enabled = true,
    events = { /* ... */ }
)
```

**Triggers fired:** `OnClick` — fired when tapped.

**Notes:** stateless — no selected state. `icon` is decorative only.

## Inputs

### `Checkbox`

Renders a bare Material 3 `Checkbox` reflecting `checked` — no label is drawn, pair with a `SimpleText` inside a `Row` for a caption.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `checked` | `Boolean` | `false` | Current checked state. |
| `enabled` | `Boolean` | `true` | Forwarded directly to the underlying composable. |

**DSL example:**
```kotlin
Row {
    Checkbox(id = "accept_terms", checked = false)
    SimpleText(text = "I agree to the terms")
}
```

**Triggers fired (in this order, on every toggle):** `OnCheck` (becomes checked) or `OnUncheck` (becomes unchecked), then always `OnCheckChanged`.

**Notes:** toggling flips the state locally first (no round trip needed for the checkbox to visually update); the holder exposes `checked` by `id` for `GetData`/`EvaluateData`. `style` defaults to wrap-content on both axes.

### `Switch`

Renders a bare Material 3 `Switch` reflecting `checked` — no label or thumb icon is drawn, pair with a `SimpleText` inside a `Row` for a caption.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `checked` | `Boolean` | `false` | Current on/off state. |
| `enabled` | `Boolean` | `true` | Forwarded directly to the underlying composable. |

**DSL example:**
```kotlin
Switch(
    id = "notifications",
    checked = true,
    events = {
        UpdateData(trigger = EventTriggers.onCheckChanged(), updates = { /* ... */ })
    }
)
```

**Triggers fired (in this order, on every toggle):** `OnCheck` (turns on) or `OnUncheck` (turns off), then always `OnCheckChanged`.

**Notes:** same local-first toggle mechanism as `Checkbox`; the holder exposes `checked` by `id` for `GetData`/`EvaluateData`.

### `RadioButton`

Renders a bare Material 3 `RadioButton` reflecting `selected` — no label is drawn, pair with a `SimpleText` inside a `Row` for a caption.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `selected` | `Boolean` | `false` | Current selection state. |
| `enabled` | `Boolean` | `true` | Forwarded directly to the underlying composable. |
| `groupId` | `String` | required | Groups radio buttons together for mutual exclusion — see Notes. |

**DSL example:**
```kotlin
Column {
    RadioButton(
        id = "plan_free",
        selected = true,
        groupId = "plan"
    )
    RadioButton(
        id = "plan_pro",
        selected = false,
        groupId = "plan"
    )
}
```

**Triggers fired:** `OnSelect` — fired only on the radio button that becomes selected. Tapping an already-selected radio fires nothing; radio buttons that lose selection fire nothing either.

**Notes:** mutual exclusion is handled entirely client-side — tapping a radio button broadcasts a group event that every `RadioButton` holder with the same `groupId` reacts to, flipping its own `selected` without any server round trip or `UpdateTiles` needed to clear the others. Radio buttons don't need to be siblings for this to work; only `groupId` matters. The holder exposes `selected` by `id` for `GetData`/`EvaluateData`. `style` defaults to wrap-content on both axes.

### `DropdownList`

Renders a Material 3 `ExposedDropdownMenuBox` — a read-only anchor field showing the selected option's label plus a dropdown listing every entry in `options`. `kind` picks the anchor composable: `FILLED` → `TextField`, `OUTLINED` → `OutlinedTextField`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `options` | `List<SelectOption>` (`id`, `label`) | required | The entries listed in the dropdown menu. |
| `selectedOptionId` | `String` | required | Id of the option whose label is shown on the anchor. |
| `enabled` | `Boolean` | `true` | Forwarded to the underlying composable. |
| `kind` | `Kind` (`FILLED`/`OUTLINED`) | `outlinedDropdownList()` | Anchor field variant. |
| `supportingText` | `String?` | `null` | Helper text shown below the field. |
| `state` | `State` (`NORMAL`/`ERROR`) | `normalDropdownList()` | `ERROR` switches the field into Material's error styling. |

**DSL example:**
```kotlin
DropdownList(
    id = "country",
    options = listOf(selectOption("br", "Brazil"), selectOption("us", "United States")),
    selectedOptionId = "br",
    kind = outlinedDropdownList()
)
```

**Triggers fired:** `OnDropdownListOpen` (anchor tapped while closed) → `OnDropdownListItemSelected` (item picked, carries the picked id as incoming data) → `OnDropdownListClose` (always fires whenever the menu closes: after picking an item, tapping the anchor while open, or dismissing by tapping outside).

**Notes:** selecting an item and closing the menu are both handled client-side, with no round trip needed. When `selectedOptionId` matches none of `options`, the anchor renders empty but the menu still lists every option, so the user can recover by picking one.

### `DatePicker`

Renders a read-only text field that opens a Material 3 `DatePickerDialog` when tapped. `kind` picks the field composable: `FILLED` → `TextField`, `OUTLINED` → `OutlinedTextField`. Always shows a `calendar_month` leading icon. Typing is impossible — the field is `readOnly`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `selectedDate` | `String?` | `null` | Selected date as an ISO string (`yyyy-MM-dd`); shown empty when `null`. |
| `enabled` | `Boolean` | `true` | Forwarded to the underlying composable. |
| `kind` | `Kind` (`FILLED`/`OUTLINED`) | `outlinedDatePicker()` | Field variant. |
| `confirmLabel` | `String` | required | Label of the dialog's confirm button. |
| `cancelLabel` | `String` | required | Label of the dialog's cancel button. |
| `supportingText` | `String?` | `null` | Helper text shown below the field. |
| `state` | `State` (`NORMAL`/`ERROR`) | `normalDatePicker()` | `ERROR` switches the field into Material's error styling. |

**DSL example:**
```kotlin
DatePicker(
    id = "birth_date",
    confirmLabel = "OK",
    cancelLabel = "Cancel",
    kind = outlinedDatePicker()
)
```

**Triggers fired:** `OnDatePickerOpen` (field pressed while closed) → `OnDateSelected` (confirm pressed, the ISO date string is the event's incoming data) → `OnDatePickerClose` (always fires whenever the dialog closes: after confirming, on cancel, or on dismiss).

**Notes:** the confirm button stays disabled until a date is picked, so a confirm always yields a date. The renderer converts the ISO string to/from epoch millis only to drive Compose's `DatePickerState` — the schema always stores/produces the ISO string. All of open/confirm/cancel is handled client-side, no round trip needed. `selectedDate` produces no entry at all for `GetData`/`EvaluateData` when `null`, rather than an empty string.

### `TimePicker`

Renders a read-only text field that opens a Material 3 `TimePickerDialog` when tapped. `kind` picks the field composable the same way as `DatePicker`. Always shows an `alarm` leading icon. The dialog is always 24-hour, vertical layout; typing is impossible.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `selectedTime` | `String?` | `null` | Selected time as an ISO string (`HH:mm`); shown empty when `null`. Dialog opens at `00:00` when `null`. |
| `enabled` | `Boolean` | `true` | Forwarded to the underlying composable. |
| `kind` | `Kind` (`FILLED`/`OUTLINED`) | `outlinedTimePicker()` | Field variant. |
| `confirmLabel` | `String` | required | Label of the dialog's confirm button. |
| `cancelLabel` | `String` | required | Label of the dialog's cancel button. |
| `supportingText` | `String?` | `null` | Helper text shown below the field. |
| `state` | `State` (`NORMAL`/`ERROR`) | `normalTimePicker()` | `ERROR` switches the field into Material's error styling. |

**DSL example:**
```kotlin
TimePicker(
    id = "reminder_time",
    confirmLabel = "OK",
    cancelLabel = "Cancel"
)
```

**Triggers fired:** `OnTimePickerOpen` → `OnTimeSelected` (confirm pressed, ISO time string as incoming data) → `OnTimePickerClose` (always fires on any close path).

**Notes:** same client-side open/confirm/cancel handling as `DatePicker`; `selectedTime` produces no `GetData`/`EvaluateData` entry when `null`.

### `TextField`

Renders a Material 3 text input field. `kind` picks the composable: `FILLED` → `TextField`, `OUTLINED` → `OutlinedTextField`. `label`, `prefixText`, `suffixText`, `supportingText` and `placeholder` render only when non-null.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `value` | `String` | `""` | Current text content. |
| `enabled` | `Boolean` | `true` | Forwarded to the underlying composable. |
| `leadingIcon` | `IconSchema?` | `null` | Icon before the text — plain by default (see `clickableLeadingIcon`). |
| `clickableLeadingIcon` | `Boolean` | `false` | When `true`, wraps `leadingIcon` in an `IconButton` that fires `OnLeadingIconClick`. |
| `trailingIcon` | `IconSchema?` | `null` | Icon after the text — plain by default (see `clickableTrailingIcon`). |
| `clickableTrailingIcon` | `Boolean` | `true` | When `true`, wraps `trailingIcon` in an `IconButton` that fires `OnTrailingIconClick`. |
| `prefixText` | `String?` | `null` | Fixed text rendered inside the field, before the value. |
| `suffixText` | `String?` | `null` | Fixed text rendered inside the field, after the value. |
| `placeholder` | `String?` | `null` | Text shown when `value` is empty. |
| `label` | `String?` | `null` | Floating label. |
| `supportingText` | `String?` | `null` | Helper text shown below the field. |
| `minLines` | `Int` | `1` | Minimum number of lines reserved. |
| `maxLines` | `Int` | `Int.MAX_VALUE` | Maximum number of lines shown. |
| `kind` | `Kind` (`FILLED`/`OUTLINED`) | `outlinedTextField()` | Field variant. |
| `state` | `State` (`NORMAL`/`ERROR`) | `normalTextField()` | `ERROR` switches the field into Material's error styling. |
| `keyboardOptions` | `KeyboardOptions?` | `null` | Capitalization, keyboard type, IME action, autocorrect, show-on-focus — see helpers below. |
| `visualTransformation` | `VisualTransformation?` | `null` | `None`, `Password` (dots) via `keyboardVisualTransformationPassword()`, or `Custom(mask)` via `keyboardVisualTransformationCustom(mask)`. |

**DSL example:**
```kotlin
TextField(
    id = "password",
    label = "Password",
    visualTransformation = keyboardVisualTransformationPassword(),
    events = { /* OnTextChanged, keyboard actions, etc. */ }
)
```

**Triggers fired:** `OnTextChanged` (every keystroke where the text actually changes vs. `value`, new text as incoming data) — plus, depending on `keyboardOptions.imeAction`, exactly one of `OnKeyboardDone`/`OnKeyboardGo`/`OnKeyboardNext`/`OnKeyboardPrevious`/`OnKeyboardSearch`/`OnKeyboardSend` — plus `OnLeadingIconClick`/`OnTrailingIconClick`, only when the matching `clickable*Icon` flag is `true`.

**Notes:** `clickableTrailingIcon` defaults to `true` while `clickableLeadingIcon` defaults to `false` — a real asymmetry, not an oversight (matches the common "clear button on the right" pattern). The renderer re-syncs its local cursor state whenever the server pushes a new `value` (e.g. via `UpdateTiles`), placing the caret at the end of the new text — editing a field the user is actively focused on can move their cursor. `GetData`/`EvaluateData` see no entry at all when `value` is empty, not an empty string.

## Images & icons

### `Icon`

Renders a single Material Symbol described by `icon` — its name, color, size and style. The standalone icon tile; `style` applies to the icon's own layout node.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `icon` | `IconSchema` | required | Material Symbol name, color, size and style. |

**DSL example:**
```kotlin
Icon(id = "star", icon = icon(name = "star", color = color(ColorSchema.Theme.Color.PRIMARY)))
```

**Triggers fired:** none — not even `OnDisplay`. Not clickable; use `IconButton` for a tappable icon, or wrap this tile in a clickable container.

**Notes:** `style` defaults to wrap-content on both axes.

### `Image`

Renders a Compose `Image` from a drawable bundled with the client application. `resourceName` is looked up in the app's `DrawableResourcesHolder` (populated by the app-host at startup) — when nothing is registered under that name, **nothing is rendered at all**, no fallback, no error trigger.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `resourceName` | `String` | required | Name looked up in the client's `DrawableResourcesHolder`. |
| `contentDescription` | `String?` | `null` | Accessibility description. |
| `contentScale` | `ContentScale` (`CROP`/`FIT`/`FILL_HEIGHT`/`FILL_WIDTH`/`INSIDE`/`FILL_BOUNDS`) | `imageFitContentScale()` | How the image scales to its bounds. |
| `alpha` | `Float` | `1.0f` | Opacity. |
| `alignment` | `AlignmentSchema.TwoDimensional` | `alignToCenter()` | Placement of the image within its bounds when the aspect ratio doesn't fill them. |

**DSL example:**
```kotlin
Image(
    id = "logo",
    resourceName = "app_logo",
    contentScale = imageFitContentScale()
)
```

**Triggers fired:** none — not clickable.

**Notes:** use `AsyncImage` for images from the network or raw bytes — this tile can only show assets bundled with the client app.

### `AsyncImage`

Renders a Coil `AsyncImage` loading its content from `model` — a remote URL, raw bytes, or base64 (decoded client-side before being handed to Coil).

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `model` | `Model` (`Url(url)`/`ArrayOfBytes(byteArray)`/`Base64(base64)`) | required | Image source — `urlImageModel(url)`, `arrayOfBytesImageModel(bytes)`, `base64ImageModel(base64)`. |
| `contentDescription` | `String?` | `null` | Accessibility description. |
| `contentScale` | `ContentScale` (`CROP`/`FIT`/`FILL_HEIGHT`/`FILL_WIDTH`/`INSIDE`/`FILL_BOUNDS`) | `fitContentScale()` | How the image scales to its bounds. |
| `alpha` | `Float` | `1.0f` | Opacity. |
| `clipToBounds` | `Boolean` | `true` | Whether the image is clipped to its layout bounds. |
| `alignment` | `AlignmentSchema.TwoDimensional` | `alignToCenter()` | Placement within its bounds. |

**DSL example:**
```kotlin
AsyncImage(
    id = "avatar",
    model = urlImageModel("https://example.com/avatar.png"),
    contentScale = cropContentScale(),
    events = {
        AddTiles(
            trigger = EventTriggers.onAsyncImageLoadFailure(),
            groupingTileId = "avatar_box",
            tiles = { /* fallback */ }
        )
    }
)
```

**Triggers fired:** `OnAsyncImageLoadStart` (Coil enters loading state), `OnAsyncImageLoadSuccess` (decoded and drawn), `OnAsyncImageLoadFailure` (load failed) — all three can fire more than once over the tile's lifetime, including on every reload triggered by `model` changing.

**Notes:** not clickable, fires no `OnDisplay`. There's no built-in placeholder or error image — render one yourself by reacting to the load triggers.

## Containers & layout

### `AdaptiveVisibility`

Conditionally renders `tiles` based on the current window size class. Children are composed only when **both** `widthVisibility` and `heightVisibility` are satisfied — otherwise nothing is emitted at all (not composed, not merely hidden).

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | The children, composed only when both breakpoints are satisfied. |
| `widthVisibility` | `WidthVisibility` (`VisibleFrom(bp)`/`VisibleUntil(bp)`, `bp` ∈ Compact/Medium/Expanded/Large/ExtraLarge) | `widthVisibleUntilExtraLarge()` | Width condition — `VisibleFrom` satisfied above the breakpoint (exclusive); `VisibleUntil` satisfied at or below it (inclusive). |
| `heightVisibility` | `HeightVisibility` (same shape, `bp` ∈ Compact/Medium/Expanded — 3 levels, not 5) | `heightVisibleUntilExpanded()` | Height condition, same semantics as width. |

**DSL example:**
```kotlin
AdaptiveVisibility(
    widthVisibility = widthVisibleFrom(WidthBreakpoint.Medium)
) {
    NavigationRail(/* ... */)
}
```

**Triggers fired:** `OnDisplay` (always, once, regardless of whether the breakpoints are satisfied), `OnWidthBreakpointSatisfied`/`OnWidthBreakpointNotSatisfied` (on first composition and every width change), `OnHeightBreakpointSatisfied`/`OnHeightBreakpointNotSatisfied` (same, for height).

**Notes:** width and height conditions are evaluated independently — one can report satisfied while the other reports not satisfied, and in that case the children still stay hidden. Width has 5 breakpoint levels, height only 3 — a real asymmetry in the schema. When both conditions hold, children are hosted in a `Box` carrying this tile's `style`/`visibility`.

### `Box`

Renders a Compose `Box` stacking `tiles` on top of each other in declaration order (later children paint above earlier ones), all positioned by `alignment`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | `{}` (empty) | Stacked children. |
| `alignment` | `AlignmentSchema.TwoDimensional` | `alignToTopStart()` | Placement applied to every child. |

**DSL example:**
```kotlin
Box(alignment = alignToCenter()) {
    Image(id = "cover", resourceName = "hero")
    SimpleText(text = "Welcome", color = color(ColorSchema.Theme.Color.ON_PRIMARY))
}
```

**Triggers fired:** `OnDisplay` (always), `OnClick`/`OnLongPress` (only if the matching event is declared on this tile — otherwise the box isn't made interactive at all).

**Notes:** never scrollable, publishes no scope `CompositionLocal` — children can't use `weight` or other column/row scope modifiers inside a `Box`.

### `Card`

Renders a Material 3 card hosting `tiles` in a `ColumnScope`. `kind` picks the composable: `DEFAULT` → `Card`, `ELEVATED` → `ElevatedCard`, `OUTLINED` → `OutlinedCard`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | Children, stacked vertically. |
| `kind` | `Kind` (`DEFAULT`/`ELEVATED`/`OUTLINED`) | `defaultCard()` | Visual variant — `defaultCard()`/`elevatedCard()`/`outlinedCard()`. |

**DSL example:**
```kotlin
Card(kind = elevatedCard()) {
    SimpleText(text = "Order #1234")
    SimpleText(text = "Delivered", color = color(ColorSchema.Theme.Color.PRIMARY))
}
```

**Triggers fired:** `OnDisplay` (always), `OnClick` (always — unlike `Box`, the Material `Card` composable always has an `onClick` slot, so this fires on tap regardless of whether an `OnClick` event is declared).

**Notes:** publishes `ColumnScope` (children can use `weight`) and clears the lazy-item scope.

### `Column`

Renders a Compose `Column` stacking `tiles` vertically, spaced by `arrangement` and aligned horizontally by `alignment`. When `scrollable` is `true`, gets a `verticalScroll` modifier — every child is still composed eagerly (not lazily), so prefer `LazyColumn` for long lists.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | Children, stacked vertically. |
| `filterChildrenByTerm` | `String?` | `null` | When set, only renders children whose `searchableTerms` contain this term (case-insensitive substring); children without `searchableTerms` are filtered out. |
| `arrangement` | `ArrangementSchema.Vertical` | `arrangeVerticallyToTop()` | Spacing between children along the main axis. |
| `alignment` | `AlignmentSchema.Horizontal` | `alignHorizontallyToStart()` | Cross-axis alignment. |
| `scrollable` | `Boolean` | `false` | Whether the column can be scrolled vertically. |

**DSL example:**
```kotlin
Column(arrangement = arrangeVerticallySpacedBy(12), scrollable = true) {
    SimpleText(text = "Item 1")
    SimpleText(text = "Item 2")
}
```

**Triggers fired:** `OnDisplay` (always), `OnClick`/`OnLongPress` (only if declared), `OnScrolled` (fires when scroll direction changes — `ScrollDirection.Bottom` forward, `ScrollDirection.Top` backward; only meaningful when `scrollable` is `true`).

**Notes:** publishes `ColumnScope` (children can use `weight`) and clears the lazy-item scope. Also listens on the screen broadcast channel for programmatic scroll-to-top/bottom/offset commands addressed to its `id` — the offset variant is a **pixel offset**, not an item index (contrast with `LazyColumn`, below).

### `LazyColumn`

Renders a Compose `LazyColumn` over `tiles` — one lazy item per child, keyed by the child's `id`. Only visible children are composed, making this the tile for long or paginated lists.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | Children, virtualized. |
| `filterChildrenByTerm` | `String?` | `null` | Same substring filter as `Column`. |
| `arrangement` | `ArrangementSchema.Vertical` | `arrangeVerticallyToTop()` | Spacing between items. |
| `alignment` | `AlignmentSchema.Horizontal` | `alignHorizontallyToStart()` | Cross-axis alignment. |
| `scrollThreshold` | `Int?` | `null` | Infinite-scroll pagination guard — see Notes. |
| `considerLoadingItemAtEndOnThresholdReached` | `Boolean` | `true` | Requires the list to have grown by more than one item to re-fire the threshold trigger, accounting for a trailing loading placeholder. |
| `displayScrollbar` | `Boolean` | `false` | Draws a vertical scrollbar on the trailing edge — honoured on every platform, typically useful on desktop/web. |

**DSL example:**
```kotlin
LazyColumn(scrollThreshold = 5, displayScrollbar = true) {
    items.forEach { item -> Card(id = item.id) { SimpleText(text = item.title) } }
}
```

**Triggers fired:** `OnDisplay` (always), `OnClick` (only if declared), `OnScrolled` (`ScrollDirection.Bottom`/`Top`), `OnScrollThresholdReached` (only when `scrollThreshold` is set — fires at most once per item count, not again until the list actually grows).

**Notes:** publishes `LazyItemScope` per item (children get `animateItem`/`fillParentMaxSize`, but **not** `ColumnScope.weight`) and clears the column scope. Programmatic scroll-to-item commands take a **child index**, not a pixel offset (contrast with the non-lazy `Column`).

### `LazyRow`

Horizontal counterpart of `LazyColumn` — same virtualization, pagination and scrollbar mechanics, laid out horizontally.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | Children, virtualized. |
| `filterChildrenByTerm` | `String?` | `null` | Same substring filter. |
| `arrangement` | `ArrangementSchema.Horizontal` | `arrangeHorizontallyToStart()` | Spacing between items. |
| `alignment` | `AlignmentSchema.Vertical` | `alignVerticallyToTop()` | Cross-axis alignment. |
| `scrollThreshold` | `Int?` | `null` | Same pagination guard as `LazyColumn`. |
| `considerLoadingItemAtEndOnThresholdReached` | `Boolean` | `true` | Same as `LazyColumn`. |
| `displayScrollbar` | `Boolean` | `false` | Draws a horizontal scrollbar along the bottom edge. |

**DSL example:**
```kotlin
LazyRow(arrangement = arrangeHorizontallySpacedBy(8)) {
    tags.forEach { tag -> AssistChip(id = tag.id, text = tag.label) }
}
```

**Triggers fired:** `OnDisplay`, `OnClick` (only if declared), `OnScrolled` (`ScrollDirection.End`/`Start`), `OnScrollThresholdReached` (same semantics as `LazyColumn`).

**Notes:** publishes `LazyItemScope` per item (not `RowScope.weight`), clears the row and flow-row scopes. Scroll-to-item commands take a child index.

### `Row`

Renders a Compose `Row` laying `tiles` out horizontally, spaced by `arrangement` and aligned vertically by `alignment`. When `scrollable` is `true`, gets a `horizontalScroll` modifier — children still composed eagerly, prefer `LazyRow` for long lists.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | Children, laid out horizontally. |
| `filterChildrenByTerm` | `String?` | `null` | Same substring filter as `Column`. |
| `arrangement` | `ArrangementSchema.Horizontal` | `arrangeHorizontallyToStart()` | Spacing between children. |
| `alignment` | `AlignmentSchema.Vertical` | `alignVerticallyToTop()` | Cross-axis alignment. |
| `scrollable` | `Boolean` | `false` | Whether the row can be scrolled horizontally. |

**DSL example:**
```kotlin
Row(arrangement = arrangeHorizontallySpacedBy(8), alignment = alignVerticallyToCenter()) {
    Icon(icon = icon("info"))
    SimpleText(text = "Info")
}
```

**Triggers fired:** `OnDisplay`, `OnClick`/`OnLongPress` (only if declared), `OnScrolled` (`ScrollDirection.End`/`Start`; only meaningful when `scrollable` is `true`).

**Notes:** publishes `RowScope` (children can use `weight`), clears the lazy-item and flow-row scopes. Scroll-to-offset commands are a pixel offset, same as `Column`.

### `SelectionContainer`

Wraps `tiles` in a Compose `SelectionContainer`, making text rendered by its descendants selectable and copyable with the platform's selection handles and context menu.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | `{}` (empty) | Children whose text becomes selectable. |

**DSL example:**
```kotlin
SelectionContainer {
    SimpleText(text = "This paragraph can be selected and copied.")
}
```

**Triggers fired:** none — not even `OnDisplay`. `events` declared on this tile are never fired; wire events on the children instead.

**Notes:** children are rendered without a scope `CompositionLocal` — behaves like a `Box` for layout, put a `Column`/`Row` inside for a specific arrangement.

### `Shimmer`

Renders a `Box` hosting `tiles` with a continuously animated shimmer effect applied over the whole subtree — the standard way to build a skeleton/loading placeholder out of plain tiles.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | The placeholder shapes to shimmer over. |

**DSL example:**
```kotlin
Shimmer {
    Column {
        Box(style = { size(width = fillHorizontally(), height = fixedVertically(16)) }) {}
    }
}
```

**Triggers fired:** `OnDisplay` — always, once.

**Notes:** the shimmer is purely visual — children stay interactive, so build the placeholder out of non-clickable tiles to avoid confusing taps. The tile itself is never clickable; children are laid out with `Box` semantics (stacked, no scope `CompositionLocal`).

### `Carousel`

Renders a Material 3 horizontal carousel over `tiles`, one child per item. `type` picks the composable: `MultiBrowse` → `HorizontalMultiBrowseCarousel` (items vary in size, edges shrink), `Uncontained` → `HorizontalUncontainedCarousel` (fixed item width).

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | One child per carousel item. |
| `type` | `CarouselTypeSchema` — `multiBrowse(preferredItemWidth, minSmallItemWidth?, maxSmallItemWidth?)` or `uncontained(itemWidth)` | required | Layout strategy — see above. |
| `itemSpacing` | `Int` | `0` | Gap between items, dp. |
| `contentPadding` | `Int` | `0` | Horizontal padding around the carousel content, dp. |
| `userScrollEnabled` | `Boolean` | `true` | Whether the user can swipe manually. |

**DSL example:**
```kotlin
Carousel(type = multiBrowse(preferredItemWidth = 280)) {
    items.forEach { item -> Card(id = item.id) { Image(resourceName = item.image) } }
}
```

**Triggers fired:** `OnDisplay` (always), `OnPageChanged` (fires once per matching direction when the current item changes — `Direction.Any` always, `Direction.Start`/`Direction.End` when landing on the first/last item, `Direction.Index(item)` for the new index; the initial item doesn't fire; carries the new item index as `incomingData`).

**Notes:** shares its scroll-control broadcast channel with `Pager` — the same `ScrollColumnTile`/`ScrollPagerTile`-style commands addressed to this tile's `id` drive it (begin/end/next/previous, clamped to the valid range, so requesting past the ends is a no-op). Children are rendered by index, no scope `CompositionLocal`.

### `FlexBox`

Renders a Compose `FlexBox` (experimental) hosting `tiles` with CSS-flexbox semantics.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | Children, laid out with flexbox rules. |
| `filterChildrenByTerm` | `String?` | `null` | Same substring filter as `Column`. |
| `direction` | `FlexDirectionSchema` (`Row`/`RowReverse`/`Column`/`ColumnReverse`) | `flexDirectionRow()` | Main axis and its reversal. |
| `justifyContent` | `FlexJustifyContentSchema` (`Start`/`Center`/`End`/`SpaceBetween`/`SpaceAround`/`SpaceEvenly`) | `flexJustifyStart()` | Distribution along the main axis. |
| `alignItems` | `FlexAlignItemsSchema` (`Start`/`End`/`Center`/`Stretch`/`Baseline`) | `flexAlignItemsStart()` | Cross-axis alignment within a line. |
| `alignContent` | `FlexAlignContentSchema` (`Start`/`End`/`Center`/`Stretch`/`SpaceBetween`/`SpaceAround`) | `flexAlignContentStart()` | Distribution of the lines themselves — only takes effect when `wrap` allows multiple lines. |
| `wrap` | `FlexWrapSchema` (`NoWrap`/`Wrap`/`WrapReverse`) | `flexNoWrap()` | Whether items wrap onto new lines. |
| `columnGap` | `Int` | `0` | Column gap, dp. |
| `rowGap` | `Int` | `0` | Row gap, dp. |

**DSL example:**
```kotlin
FlexBox(wrap = flexWrap(), justifyContent = flexJustifySpaceBetween()) {
    tags.forEach { tag -> AssistChip(id = tag.id, text = tag.label) }
}
```

**Triggers fired:** `OnDisplay` (always), `OnClick` (only if declared).

**Notes:** publishes `FlexBoxScope` (children get grow/shrink/basis/align-self modifiers). Never scrollable, every child composed eagerly.

### `FlowRow`

Renders a Compose `FlowRow` laying `tiles` out horizontally, wrapping onto new lines when they no longer fit.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | Children, wrapped across lines as needed. |
| `filterChildrenByTerm` | `String?` | `null` | Same substring filter as `Column`. |
| `horizontalArrangement` | `ArrangementSchema.Horizontal` | `arrangeHorizontallyToStart()` | Spacing between items within a line. |
| `verticalArrangement` | `ArrangementSchema.Vertical` | `arrangeVerticallyToTop()` | Spacing between the lines themselves. |
| `maxItemsInEachRow` | `Int` | `Int.MAX_VALUE` | Caps how many children a single line may hold. |

**DSL example:**
```kotlin
FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(8)) {
    filters.forEach { f -> FilterChip(id = f.id, text = f.label, selected = f.active) }
}
```

**Triggers fired:** `OnDisplay` (always), `OnClick` (only if declared).

**Notes:** publishes `FlowRowScope` (children get `weight` and `fillMaxRowHeight`), clears the row and lazy-item scopes. Never scrollable, every child composed eagerly. Stable (not experimental), unlike `FlexBox`/`Grid`.

### `Grid`

Renders a Compose `Grid` (experimental, CSS-grid-like) hosting `tiles`. `columns`/`rows` declare the track template.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | Children, placed onto the grid. |
| `filterChildrenByTerm` | `String?` | `null` | Same substring filter as `Column`. |
| `columns` | `List<GridTrackSchema>` | required | Column tracks — `gridColumnFixed(dp)`, `gridColumnFraction(f)`, `gridColumnFlexible(fr)`, `gridColumnAuto()`, `gridColumnMaxContent()`, `gridColumnMinContent()`. |
| `rows` | `List<GridTrackSchema>` | required (builder), empty allowed in schema | Row tracks — same helpers with `gridRow*` prefix. Empty derives row tracks implicitly. |
| `columnGap` | `Int` | `0` | Column gap, dp. |
| `rowGap` | `Int` | `0` | Row gap, dp. |
| `flow` | `GridFlowSchema` (`Row`/`Column`) | `flowGridThroughRows()` | Whether children are placed row-first or column-first. |

**DSL example:**
```kotlin
Grid(
    columns = listOf(gridColumnFlexible(1f), gridColumnFlexible(1f)),
    rows = emptyList(),
    columnGap = 8, rowGap = 8
) {
    products.forEach { p -> Card(id = p.id) { Image(resourceName = p.image) } }
}
```

**Triggers fired:** `OnDisplay` (always), `OnClick`/`OnLongPress` (only if declared).

**Notes:** the DSL function's `rows` parameter has **no default**, even though the underlying schema defaults it to an empty list — pass `rows = emptyList()` explicitly to get the "derive row tracks implicitly" behavior via the DSL. Publishes `GridScope` (row/column span and placement modifiers). Never scrollable, every child composed eagerly.

### `LazyTiles`

Renders a `Column` whose content is fetched from the network at display time — the only tile that issues its own network call directly, outside the event pipeline.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `url` | `String` | required | Endpoint fetched to load the real content. |
| `method` | `HttpMethod` | `HttpMethod.GET` | HTTP method used for the fetch. |
| `body` | `AnySerializable?` | `null` | Request body. |
| `headers` | `Map<String, String>?` | `null` | Request headers. |
| `failureTiles` | `List<TileSchema>` | `{}` (empty) | Rendered when the fetch or decode fails. |
| `placeholderTiles` | `List<TileSchema>` | `{}` (empty) | Rendered while the fetch is in flight. |

**DSL example:**
```kotlin
LazyTiles(
    url = "/api/recommendations",
    placeholderTiles = { Shimmer { /* skeleton */ } },
    failureTiles = { SimpleText(text = "Couldn't load recommendations") }
)
```

**Triggers fired:** `OnDisplay` (always), `OnLoadTilesStart` (right before the request), then exactly one of `OnLoadTilesSuccess` (response received and decoded) or `OnLoadTilesFailure` (transport or decode error — the `Throwable` is the incoming data).

**Notes:** the expected response is a JSON array of tile schemas, replacing `placeholderTiles`/`failureTiles` once loaded. The fetch fires once, on the IO dispatcher, via a single-shot effect — since it bypasses the event pipeline entirely, it's not affected by event chaining and there's no `trigger` to hook the request itself to. Reloading is done remotely via the `ReloadLazyTiles` event, targeting this tile's `id`. Children are laid out in a plain `Column`, no scope `CompositionLocal`, never scrollable.

### `Pager`

Renders a Compose `HorizontalPager` over `tiles`, one page per child.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | One child per page. |
| `pageSize` | `PageSizeSchema` (`Fill`/`Fixed(dp)`) | `pageFill()` | Full-width pages or a fixed width — `pageFill()`/`pageFixed(dp)`. |
| `pageSpacing` | `Int` | `0` | Gap between pages, dp. |
| `contentPadding` | `Int` | `0` | Horizontal padding around the pager content, dp. |
| `beyondViewportPageCount` | `Int` | `0` | How many off-screen pages stay composed. |

**DSL example:**
```kotlin
Pager(pageSize = pageFill()) {
    onboardingSteps.forEach { step -> Column(id = step.id) { /* ... */ } }
}
```

**Triggers fired:** `OnDisplay` (always), `OnPageChanged` (fires once per matching direction on every settled page change — `Direction.Any` always, `Direction.Start`/`End` on first/last page, `Direction.Index(page)` for the new index; the initial page doesn't fire; carries the new page index as `incomingData`. Wiring several directions means several chains run for the same page change).

**Notes:** horizontal only. Same scroll-control broadcast mechanism as `Carousel` (begin/end/next/previous, clamped to valid range). Children rendered by index, no scope `CompositionLocal`.

### `PullToRefresh`

Renders a Material 3 `PullToRefreshBox` wrapping `tiles`, showing the refresh indicator while `isRefreshing` is `true`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | `List<TileSchema>` | required | Content wrapped by the pull gesture — put a scrollable tile inside for the gesture to feel natural. |
| `isRefreshing` | `Boolean` | `false` | Whether the refresh indicator is currently shown. |

**DSL example:**
```kotlin
PullToRefresh(isRefreshing = false, events = {
    RefreshScreen(
        trigger = EventTriggers.onPull(),
        events = {
            StopRefreshing(trigger = EventTriggers.onSuccess(), tileId = "list")
        }
    )
}) {
    LazyColumn { /* ... */ }
}
```

**Triggers fired:** `OnDisplay` (always), `OnPull` (fired when the user completes a pull gesture — hook the refresh work to this).

**Notes:** pulling sets `isRefreshing = true` locally, immediately, with no server round trip — but nothing turns it back off automatically. The server must chain a `StopRefreshing` event pointing at this tile's `id` onto **both** the success and the failure branch of the refresh flow, or the indicator spins forever. Children laid out with `Box` semantics, no scope `CompositionLocal`.

## App bars

### `TopAppBar`

Renders a Material 3 top app bar. `barStyle` picks the composable: `DEFAULT` → `TopAppBar`, `CENTER_ALIGNED` → `CenterAlignedTopAppBar`, `MEDIUM` → `MediumTopAppBar`, `LARGE` → `LargeTopAppBar`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `title` | tile block | required | Fills the title slot — most often a `SimpleText`. |
| `navigationIcon` | tile block? | `null` | Fills the leading slot — typically an `IconButton`. Empty slot when omitted. |
| `actions` | tile block? | `null` | Fills the trailing slot (`RowScope`). Empty slot when omitted. |
| `barStyle` | `TopAppBarStyle` (`DEFAULT`/`CENTER_ALIGNED`/`MEDIUM`/`LARGE`) | `defaultTopAppBar()` | Visual variant — `defaultTopAppBar()`, `centerAlignedTopAppBar()`, `mediumTopAppBar()`, `largeTopAppBar()`. |

**DSL example:**
```kotlin
TopAppBar(
    title = { SimpleText(text = "Settings") },
    navigationIcon = { IconButton(icon = icon("arrow_back"), events = { NavigateUp(trigger = EventTriggers.onClick(), navigatorId = "root") }) },
    barStyle = centerAlignedTopAppBar()
)
```

**Triggers fired:** none — not clickable. Wire events on the slot tiles (typically `IconButton`s inside `navigationIcon`/`actions`) instead.

**Notes:** `title` is required — omitting it throws at grid-build time on the server. `title` and `navigationIcon` only keep the **last** tile declared inside their block if more than one is added; declare exactly one. The bar has no scroll behavior, so `MEDIUM`/`LARGE` don't collapse as content scrolls.

### `BottomAppBar`

Renders a Material 3 `BottomAppBar` with `actions` laid out on the leading side and an optional `floatingActionButton` docked at the trailing edge.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `actions` | `List<TileSchema>` | required | Tiles laid out in a `RowScope` on the leading side. |
| `floatingActionButton` | tile block? | `null` | Any tile docked at the trailing edge, typically a `FloatingActionButton`. |

**DSL example:**
```kotlin
BottomAppBar(
    actions = { IconButton(icon = icon("search"), events = { /* ... */ }) },
    floatingActionButton = { FloatingActionButton(icon = icon("add"), size = mediumFloatingActionButon()) }
)
```

**Triggers fired:** none — not clickable. Wire events on the action tiles instead.

**Notes:** `floatingActionButton` also keeps only the last declared tile if more than one is added.

## Navigation

### `NavigationBar`

Renders a Material 3 `NavigationBar` with one `NavigationBarItem` per entry in `items`. The selected item's icon draws filled; the rest outlined.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `items` | `List<NavigationBarItem>` (`id`, `icon`, `label?`) — built via `item(id, icon, label?)` | required | Entries, one per bar item. |
| `selectedItemId` | `String` | required | Id of the currently highlighted item. |

**DSL example:**
```kotlin
NavigationBar(
    selectedItemId = "home",
    items = {
        item(
            id = "home",
            icon = icon("home"),
            label = "Home"
        )
        item(
            id = "profile",
            icon = icon("person"),
            label = "Profile"
        )
    },
    events = { Navigate(trigger = EventTriggers.onNavigationBarItemClick("home"), destination = "home", navigatorId = "root") }
)
```

**Triggers fired:** `OnNavigationBarItemClick` — fired on tap, carrying the tapped item's `id` (so events can be wired per item, matching the trigger's parameter). Tapping the already-selected item still fires it again.

**Notes:** selection moves locally, instantly, with no server round trip — the bar only tracks which item is highlighted; actually navigating is the job of whatever event you wire to the click. Not clickable itself, fires no `OnDisplay`.

### `NavigationRail`

Side-rail counterpart of `NavigationBar` — same selection mechanism, plus optional `header`/`footer` slots. The renderer applies 4dp horizontal / 8dp vertical padding.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `items` | `List<NavigationRailItem>` (`id`, `icon`, `label?`) — built via `addItem(id, icon, label?)` | required | Entries, one per rail item. |
| `selectedItemId` | `String` | required | Id of the currently highlighted item. |
| `header` | tile block? | `null` | Rendered above the items. |
| `footer` | tile block? | `null` | Rendered at the bottom, pushed down by a weighted spacer. |

**DSL example:**
```kotlin
NavigationRail(
    selectedItemId = "home",
    items = { addItem(id = "home", icon = icon("home"), label = "Home") },
    footer = { IconButton(icon = icon("settings"), events = { /* ... */ }) }
)
```

**Triggers fired:** `OnNavigationRailItemClick` — same semantics as `NavigationBar`'s `OnNavigationBarItemClick`.

**Notes:** the sub-builder for adding an item is named `addItem` here, not `item` (as in `NavigationBar`) — a real naming inconsistency between the two, not a typo in this catalog. Same "selection is local-only, navigation is your job" pattern as `NavigationBar`.

### `Tabs`

Renders a Material 3 tab row with one `Tab` per entry in `tabItems`. `tabType` picks the emphasis (`PRIMARY`/`SECONDARY`); `scrollable` picks between the fixed row and the scrollable one.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `selectedTabId` | `String` | required | Id of the currently selected tab. |
| `tabItems` | `List<TabItem>` (`id`, `label?`, `icon?`, `badgeText?`) — built via `addTab(id, label?, icon?, badgeText?)` | required | Entries, one per tab. |
| `tabType` | `Type` (`PRIMARY`/`SECONDARY`) | `primaryTabs()` | Visual emphasis — `primaryTabs()`/`secondaryTabs()`. |
| `scrollable` | `Boolean` | required (no default) | `false` for a fixed row, `true` for a horizontally scrollable one. |

**DSL example:**
```kotlin
Tabs(
    selectedTabId = "all",
    scrollable = false,
    tabItems = {
        addTab(id = "all", label = "All")
        addTab(
            id = "unread",
            label = "Unread",
            badgeText = ""
        )
    },
    events = { UpdateTiles(trigger = EventTriggers.onTabItemClick("unread"), updates = { /* filter list */ }) }
)
```

**Triggers fired:** `OnTabItemClick` — fired on tap, carrying the tapped tab's `id`.

**Notes:** `badgeText` on a `TabItem`: `null` = no badge, empty string = small dot badge, any other value = badge with that text — 3 states, not 2. The badge attaches to the icon if present, otherwise to the label. When `selectedTabId` matches none of `tabItems`, the first tab is highlighted as a silent fallback. An empty `tabItems` renders nothing. Selection is local-only, same as the other two navigation tiles.

### `NestedNavigationGraph`

Hosts a self-contained Navigation 3 `NavDisplay` inside a tile — a region of a screen gets its own back stack, independent of the screen it lives in.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `navigatorId` | `String` | required | Id under which this graph's `NavigationController` is registered — `Navigate`/`NavigateUp`/`NavigateClearingStack` events target it by this id. |
| `startEntryId` | `String` | required | `screenId` of the entry shown first. |
| `entries` | `NestedNavigationGraphEntryBuilderScope.() -> Unit` — built via `entry(screenId, initialTiles?, initialEvents?, failureTiles?, failureEvents?, transition?, popTransition?, predictivePopTransition?)` | required | Every screen this graph can show. |
| `defaultTransition` | `ContentTransitionSchema?` | `null` | Fallback enter/exit transition for entries that don't declare their own. |
| `defaultPopTransition` | `ContentTransitionSchema?` | `null` | Fallback transition for popping back. |
| `defaultPredictivePopTransition` | `ContentTransitionSchema?` | `null` | Fallback transition for the predictive-back gesture. |

**DSL example:**
```kotlin
NestedNavigationGraph(
    navigatorId = "settings_graph",
    startEntryId = "settings_home",
    entries = {
        entry(screenId = "settings_home")
        entry(screenId = "settings_profile")
    }
)
```

**Triggers fired:** `OnNavigationEntrySet` — fired whenever an entry is displayed, carrying that entry's `screenId`. Fires for the start destination too, and again every time navigation returns to an already-visited entry.

**Notes:** registration of the `navigatorId` and every `entry` is tied to this tile's own composition — both are undone the moment the tile leaves the screen, so events targeting this graph's `navigatorId` only work while it's actually on screen. Each entry's `initialEvents` defaults to the framework-wide `GetScreen(onDisplay()) { ChangeScreenState(onSuccess(), successState()) }` pair (see `architecture.md` §5) unless overridden. Each entry keeps its own saveable state and `ViewModelStore`, so navigating back and forth within the graph preserves screen state. The system back gesture pops this graph's own stack, not the screen's.

## Progress

### `CircularProgressIndicator`

Renders a Material 3 `CircularProgressIndicator`. `null` progress → indeterminate spinner; a value → determinate arc (`0f` empty, `1f` complete).

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `progress` | `Float?` | `null` (indeterminate) | Determinate fraction, `0f`–`1f`. |

**DSL example:**
```kotlin
CircularProgressIndicator(progress = 0.6f)
```

**Triggers fired:** none — not clickable.

**Notes:** `style` defaults to a **fixed 48×48dp** size (the only progress tile with a non-trivial style default, since a circle has no intrinsic content to wrap). Colors/stroke are Material defaults — only `style` (size, padding, background) is applied. Drive a determinate indicator over time by pushing new `progress` values via `UpdateTiles`.

### `LinearProgressIndicator`

Renders a Material 3 `LinearProgressIndicator`. Same `progress` semantics as `CircularProgressIndicator`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `progress` | `Float?` | `null` (indeterminate) | Determinate fraction, `0f`–`1f`. |

**DSL example:**
```kotlin
LinearProgressIndicator(progress = null, style = { size(width = fillHorizontally(), height = fixedVertically(4)) })
```

**Triggers fired:** none — not clickable.

**Notes:** unlike `CircularProgressIndicator`, `style` uses the plain framework default (fill/wrap) — it can stretch horizontally on its own since it's a bar, not a circle. Colors/track/cap are Material defaults.

## Feedback & overlays

### `Badge`

Renders a Material 3 `Badge`. Non-null `content` → pill-shaped badge with that text; `null` → small empty dot.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `content` | `String?` | `null` (dot form) | Text shown inside the badge. |

**DSL example:**
```kotlin
Box(alignment = alignToTopEnd()) {
    Icon(icon = icon("notifications"))
    Badge(content = "3")
}
```

**Triggers fired:** none — not clickable.

**Notes:** a standalone badge, **not** a `BadgedBox` — it doesn't attach itself to a sibling automatically. Position it yourself, typically inside a `Box` aligned over the tile it decorates (as in the example above).

### `Menu`

Renders an anchor (`tiles`) with a Material 3 `DropdownMenu` attached, listing one entry per `items`. Uses the theme's large shape, respects system bars, caps at 400dp tall (scrolls beyond that).

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | tile block | required | The anchor — laid out with `Box` semantics, carries `style`/`visibility`. |
| `items` | `List<MenuItem>` (`id`, `label`, `leadingIcon?`, `trailingIcon?`) — built via `addMenuItem(id, label, leadingIcon?, trailingIcon?)` | required | Entries in the dropdown. |
| `expanded` | `Boolean` | `false` | Whether the menu is currently open. |

**DSL example:**
```kotlin
Menu(
    expanded = false,
    items = {
        addMenuItem(
            id = "edit",
            label = "Edit",
            leadingIcon = icon("edit")
        )
        addMenuItem(
            id = "delete",
            label = "Delete",
            leadingIcon = icon("delete")
        )
    },
    events = { ToggleMenu(trigger = EventTriggers.onClick(), menuId = "item_menu") }
) {
    IconButton(icon = icon("more_vert"))
}
```

**Triggers fired:** `OnMenuItemClick` — fired when an item is tapped, carrying that item's `id`.

**Notes:** dismissing by gesture (tap outside, back) closes the menu locally, no round trip needed. Opening the menu, or closing it programmatically (e.g. after acting on a selection), requires the `ToggleMenu` event pointed at this tile's `id` — it's a toggle, not separate open/close events. `style`/`visibility` apply only to the anchor; the dropdown itself is positioned by Material and unaffected by them.

### `Popup`

Renders an anchor (`tiles`) with a Compose `Popup` containing `popupTiles` floating over it, composed only while `expanded` is `true`.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | tile block | required | The anchor. |
| `popupTiles` | tile block | required | Content of the floating popup — unstyled by default, see Notes. |
| `expanded` | `Boolean` | `false` | Whether the popup is currently shown. |
| `alignment` | `AlignmentSchema.TwoDimensional` | `alignToTopStart()` | Placement relative to the anchor's bounds — see behavior notes below. |
| `offsetX` | `Int` | `0` | Horizontal dp offset — meaning depends on `alignment` (see Notes). |
| `offsetY` | `Int` | `0` | Vertical dp offset — meaning depends on `alignment`. |
| `focusable` | `Boolean` | `false` | Whether the popup can take focus. |
| `dismissOnBackPress` | `Boolean` | `true` | Whether the back gesture dismisses it. |
| `dismissOnClickOutside` | `Boolean` | `true` | Whether tapping outside dismisses it. |

**DSL example:**
```kotlin
Popup(
    expanded = false,
    alignment = alignToBottomStart(),
    offsetY = 8,
    events = { TogglePopup(trigger = EventTriggers.onClick(), popupId = "info_popup") },
    tiles = { IconButton(icon = icon("info")) },
    popupTiles = { Card { SimpleText(text = "More information here") } }
)
```

**Triggers fired:** none. `events` declared on this tile are never fired — wire events on the anchor and popup tiles instead.

**Notes:** the meaning of `offsetX`/`offsetY` is not uniform across `alignment` values: `Top*`/`Bottom*` alignments put the popup fully above/below the anchor with `offsetY` as the gap; `CenterStart`/`CenterEnd` put it fully to the side with `offsetX` as the gap (mirrored in RTL); corner alignments flush-align the matching edges, with `offsetX` as a plain translation. The final position is always clamped to the window — a popup can never be pushed off screen. Dismissal by gesture flips `expanded` locally, no round trip; programmatic open/close is via `TogglePopup` pointed at this tile's `id`. `popupTiles` renders completely unstyled — add a `Card` or a styled `Box` inside if you want a visible surface behind the content.

### `SearchBar`

Renders a search field — a `Surface` (extra-large shape, high surface-container color) wrapping a single-line text field. The keyboard's IME action is always "Search".

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `query` | `String` | `""` | Current search text. |
| `placeholder` | `String?` | `null` | Shown when `query` is empty. |
| `leadingIcon` | tile block? | `null` | **Any tile** in the leading slot (not just `IconSchema`). |
| `trailingIcon` | tile block? | `null` | **Any tile** in the trailing slot — cross-fades out for a built-in clear button once `query` has text, see Notes. |

**DSL example:**
```kotlin
SearchBar(
    id = "search",
    placeholder = "Search tiles…",
    events = {
        TransformData(
            trigger = EventTriggers.onQueryChanged(),
            template = mapOf("filterChildrenByTerm" to "<||>"),
            events = { UpdateTiles(trigger = EventTriggers.onSuccess(), updates = { update(tileId = "results", updateData = incomingTileUpdateData()) }) }
        )
    }
)
```

**Triggers fired:** `OnQueryChanged` (every keystroke, new text as incoming data — clearing also fires it, with an empty string), `OnQueryCleared` (clear button pressed, fires right before `OnQueryChanged` above), `OnSearch` (IME "Search" pressed, current `query` as incoming data).

**Notes:** `leadingIcon`/`trailingIcon` accept **any tile**, not just an icon — the one exception among all icon-bearing fields across the tile catalog. The trailing slot is shared with a built-in clear button: while `query` is empty it shows `trailingIcon` (if set); as soon as there's text it cross-fades to `clear`, so a custom trailing icon and the clear button are never visible together. This is only the input field — no suggestion list, no expanded state; pair it with a `LazyColumn`/`Column`'s `filterChildrenByTerm` to show results, as in the example.

### `SystemBroadcastListener`

Renders `tiles` and, while they're on screen, subscribes to the app-wide system broadcast channel — every broadcast received becomes a trigger.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | tile block | required | Children, hosted in a `Box`. |

**DSL example:**
```kotlin
SystemBroadcastListener(events = { RefreshScreen(trigger = EventTriggers.onSystemBroadcast("push_received")) }) {
    Column { /* screen content */ }
}
```

**Triggers fired:** `OnSystemBroadcast` — fired per broadcast received, carrying the broadcast's own id (so events can be wired per broadcast) with the payload as incoming data.

**Notes:** this is how host-app signals (push notifications, connectivity changes, anything the app publishes on the system broadcast channel — see `architecture.md` §4) drive server-declared event flows. The subscription lives only as long as the tile is composed — off screen, broadcasts aren't observed. Multiple instances across different screens each react independently to the same broadcast.

### `Tooltip`

Wraps `tiles` in a Material 3 `TooltipBox` showing a `PlainTooltip` with `text`. Driven entirely by the platform's own gestures (long press on touch, hover on pointer) — the server never opens or observes it.

**Parameters:**

| Parameter | Type | Default | What it's for |
|---|---|---|---|
| `tiles` | tile block | required | The anchor content. |
| `text` | `String` | required | Tooltip text. |
| `position` | `Position` (`ABOVE`/`BELOW`/`LEFT`/`RIGHT`/`START`/`END`) | `tooltipPositionAbove()` | Where the tooltip appears relative to the anchor. |
| `spacing` | `Int?` | `null` (Material default) | Gap from the anchor, dp. |
| `showCaret` | `Boolean` | `false` | Whether to show the little pointer arrow. |
| `maxWidth` | `Int?` | `null` | Maximum tooltip width, dp. |
| `shape` | `ShapeSchema?` | `null` | Overrides the tooltip surface's shape. |
| `contentColor` | `ColorSchema?` | `null` | Overrides the tooltip text color. |
| `containerColor` | `ColorSchema?` | `null` | Overrides the tooltip surface color. |

**DSL example:**
```kotlin
Tooltip(text = "Copies the invite link", position = tooltipPositionBelow()) {
    IconButton(icon = icon("content_copy"), enabled = true)
}
```

**Triggers fired:** none. `events` declared on this tile are never fired — wire events on the wrapped tiles instead.

**Notes:** `style`/`visibility` apply to the `TooltipBox` (the anchor), not to the tooltip surface — that's controlled by `shape`/`contentColor`/`containerColor` above. Only plain tooltips are supported, no rich variant with a title/actions.
