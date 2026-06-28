# Mosaic — Tiles Reference

All tiles implement `TileSchema`. For each tile: purpose (from KDoc), fields with types and defaults, updatable fields, triggers dispatched, and a real DSL example.

**Base fields (every tile):** `id: String` (default `randomId()`), `events: EventSchemaBuilderScope.() -> Unit` (default `{}`), `style: StyleSchemaBuilderScope.() -> Unit`, `visibility: TileSchema.Visibility` (default `VISIBLE`), `searchableTerms: List<String>?` (default `null` — tile always shown when null)

All DSL builder functions are extensions on `TileSchemaBuilderScope`. Use wildcard import: `import dev.catbit.mosaic.server.builder.tile.builders.*` or import the specific subpackage.

---

## Text

### SimpleText
**Purpose:** Renders a non-interactive text label using Compose's `Text` composable.
**When to use:** Any static or server-updated display text — titles, labels, descriptions, values.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `text` | `String` | required | The displayed string |
| `color` | `ColorSchema?` | `null` | Falls back to `LocalTextStyle` inherited color |
| `typography` | `TypographySchema?` | `null` | Falls back to ambient `LocalTextStyle` |

**Updatable via UpdateTiles:** `text`, `color`, `typography`, `visibility`, `style`
**Triggers dispatched:** None.
**Notes:** When `color` is null the renderer uses `Color.Unspecified`. The tile respects `visibility` via the `visible()` modifier — still laid out but hidden when invisible.

**Example:**
```kotlin
SimpleText(
    text = "Welcome, ${user.name}",
    color = primaryColor(),
    typography = titleLarge()
)
```

---

## Buttons

### Button
**Purpose:** Renders a Material 3 button in one of five visual styles controlled by `buttonType`.
**When to use:** Primary call-to-action, form submission, or any clickable labeled action.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button`

**Helper functions** (import same path as above):
- `filledButton()` → `ButtonTileSchema.Type.FILLED`
- `elevatedButton()` → `ButtonTileSchema.Type.ELEVATED`
- `filledTonalButton()` → `ButtonTileSchema.Type.FILLED_TONAL`
- `outlinedButton()` → `ButtonTileSchema.Type.OUTLINED`
- `textButton()` → `ButtonTileSchema.Type.TEXT`
- `squareButton()` → `ButtonTileSchema.Shape.SQUARE`
- `roundedButton()` → `ButtonTileSchema.Shape.ROUNDED`
- `iconAtStart()` → `ButtonTileSchema.IconPosition.START`
- `iconAtEnd()` → `ButtonTileSchema.IconPosition.END`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `text` | `String` | required | Button label |
| `icon` | `IconSchema?` | `null` | Optional icon |
| `buttonType` | `ButtonTileSchema.Type` | `FILLED` | use helpers: `filledButton()`, `elevatedButton()`, etc. |
| `shape` | `ButtonTileSchema.Shape` | `ROUNDED` | use `squareButton()` or `roundedButton()` |
| `loading` | `Boolean` | `false` | Shows spinner, replaces text+icon |
| `enabled` | `Boolean` | `true` | Disables interaction when false |
| `iconPosition` | `ButtonTileSchema.IconPosition` | `START` | use `iconAtStart()` or `iconAtEnd()` |

**Updatable via UpdateTiles:** `text`, `icon`, `buttonType`, `shape`, `loading`, `enabled`, `visibility`, `style`
**Triggers dispatched:** `OnClick`, `OnLongPress`
**Notes:** When `loading = true`, text and icon are replaced by a `CircularProgressIndicator`; the button remains pressable (only `enabled` gates interactivity).

**Example:**
```kotlin
Button(
    id = "loginButton",
    text = "Entrar",
    buttonType = filledButton(),
    style = {
        size(width = fillHorizontally(max = 400), height = fixedVertically(56))
        margin(horizontal = 24, bottom = 8)
    },
    events = {
        TriggerEvent(
            eventId = "loginEvent",
            trigger = EventTriggers.onClick()
        )
    }
)
```

### IconButton
**Purpose:** Renders a Material 3 icon button (no label) in one of four visual styles.
**When to use:** Toolbar actions, icon-only interactive controls (e.g. back, search, close).
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.buttons.IconButton`

**Helper functions** (import same path as above):
- `defaultIconButton()` → `IconButtonTileSchema.Type.DEFAULT`
- `filledIconButton()` → `IconButtonTileSchema.Type.FILLED`
- `filledTonalIconButton()` → `IconButtonTileSchema.Type.FILLED_TONAL`
- `outlinedIconButton()` → `IconButtonTileSchema.Type.OUTLINED`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `icon` | `IconSchema` | required | The icon to display |
| `buttonType` | `IconButtonTileSchema.Type` | `DEFAULT` | use `defaultIconButton()`, `filledIconButton()`, etc. |
| `enabled` | `Boolean` | `true` | |

**Updatable via UpdateTiles:** `icon`, `buttonType`, `enabled`, `visibility`, `style`
**Triggers dispatched:** `OnClick`

**Example:**
```kotlin
IconButton(
    id = "backButton",
    icon = icon("arrow_back"),
    buttonType = defaultIconButton(),
    events = {
        NavigateUp(trigger = EventTriggers.onClick())
    }
)
```

### FloatingActionButton
**Purpose:** Renders a Material 3 FAB displaying a single icon, sized by `size`.
**When to use:** Primary action of a screen that needs persistent, prominent access.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.buttons.FloatingActionButton`

**Helper functions** (import same path as above):
- `defaultFloatingActionButon()` → `FloatingActionButtonTileSchema.Size.DEFAULT`
- `mediumFloatingActionButon()` → `FloatingActionButtonTileSchema.Size.MEDIUM`
- `largeFloatingActionButon()` → `FloatingActionButtonTileSchema.Size.LARGE`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `icon` | `IconSchema` | required | |
| `size` | `FloatingActionButtonTileSchema.Size` | `DEFAULT` | use `defaultFloatingActionButon()`, `mediumFloatingActionButon()`, `largeFloatingActionButon()` |
| `enabled` | `Boolean` | `true` | Note: not forwarded to composable; FAB is always interactive at Compose level |

**Updatable via UpdateTiles:** `icon`, `size`, `enabled`, `visibility`, `style`
**Triggers dispatched:** `OnClick`

**Example:**
```kotlin
FloatingActionButton(
    id = "addFab",
    icon = icon("add"),
    size = mediumFloatingActionButon(),
    events = {
        Navigate(trigger = EventTriggers.onClick(), screenId = "create_item")
    }
)
```

---

## Chips

### AssistChip
**Purpose:** Renders a Material 3 AssistChip with optional leading/trailing icons. Assist chips represent smart/automated actions that span multiple apps.
**When to use:** Actions triggered from context (e.g. "Open in Calendar", "Share", "Add to Drive").
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.chips.AssistChip`

**Helper functions** (import same path as above):
- `defaultAssistChip()` → `AssistChipTileSchema.Variant.DEFAULT` (outlined)
- `elevatedAssistChip()` → `AssistChipTileSchema.Variant.ELEVATED` (shadow, no border)

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `text` | `String` | required | |
| `leadingIcon` | `IconSchema?` | `null` | |
| `trailingIcon` | `IconSchema?` | `null` | |
| `enabled` | `Boolean` | `true` | Disabled chips do not fire clicks |
| `variant` | `AssistChipTileSchema.Variant` | `DEFAULT` | use `defaultAssistChip()` or `elevatedAssistChip()` |

**Updatable via UpdateTiles:** `style`, `visibility`, `text`, `leadingIcon`, `trailingIcon`, `enabled`, `variant`
**Triggers dispatched:** `OnClick`

**Example:**
```kotlin
AssistChip(
    id = "calendarChip",
    text = "Add to Calendar",
    leadingIcon = icon("calendar_today"),
    variant = defaultAssistChip(),
    events = {
        Navigate(trigger = EventTriggers.onClick(), screenId = "calendar")
    }
)
```

### FilterChip
**Purpose:** Renders a Material 3 FilterChip with a toggleable `selected` state. Used to filter content.
**When to use:** Category filters, tag selection, content type toggles.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.chips.FilterChip`

**Helper functions** (import same path as above):
- `defaultFilterChip()` → `FilterChipTileSchema.Variant.DEFAULT`
- `elevatedFilterChip()` → `FilterChipTileSchema.Variant.ELEVATED`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `text` | `String` | required | |
| `selected` | `Boolean` | `false` | Server-controlled — must update via UpdateTiles |
| `leadingIcon` | `IconSchema?` | `null` | Often a checkmark when selected |
| `trailingIcon` | `IconSchema?` | `null` | |
| `enabled` | `Boolean` | `true` | |
| `variant` | `FilterChipTileSchema.Variant` | `DEFAULT` | use `defaultFilterChip()` or `elevatedFilterChip()` |

**Updatable via UpdateTiles:** `style`, `visibility`, `text`, `selected`, `leadingIcon`, `trailingIcon`, `enabled`, `variant`
**Triggers dispatched:** `OnCheck` (→ selected), `OnUncheck` (→ unselected), `OnCheckChanged` (always)
**Form tile:** `produceValueWithKey` always returns `mapOf(key to tile.selected)` (`Boolean`). Always present — never null.

**Example:**
```kotlin
FilterChip(
    id = "activeFilter",
    text = "Active",
    selected = false,
    leadingIcon = icon("check"),
    events = {
        UpdateTiles(
            trigger = EventTriggers.onCheck(),
            updates = {
                update(tileId = "activeFilter", updateData = inlineTileUpdateData(mapOf("selected" to true)))
            }
        )
        UpdateTiles(
            trigger = EventTriggers.onUncheck(),
            updates = {
                update(tileId = "activeFilter", updateData = inlineTileUpdateData(mapOf("selected" to false)))
            }
        )
    }
)
```

### InputChip
**Purpose:** Renders a Material 3 InputChip representing a discrete piece of information entered or chosen by the user (tag, contact, filter value).
**When to use:** Multi-value input fields, tag lists, selected items that can be removed.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.chips.InputChip`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `text` | `String` | required | |
| `selected` | `Boolean` | `false` | |
| `leadingIcon` | `IconSchema?` | `null` | |
| `trailingIcon` | `IconSchema?` | `null` | Typically a close/remove icon (purely visual) |
| `enabled` | `Boolean` | `true` | |

**Updatable via UpdateTiles:** `style`, `visibility`, `text`, `selected`, `leadingIcon`, `trailingIcon`, `enabled`
**Triggers dispatched:** `OnCheck`, `OnUncheck`, `OnCheckChanged`
**Notes:** No elevated variant (Material 3 InputChip has no elevation). The trailing icon is inside the chip's single click area — use check/uncheck triggers to react and remove the chip via UpdateTiles/RemoveTiles.
**Form tile:** `produceValueWithKey` always returns `mapOf(key to tile.selected)` (`Boolean`). Always present — never null.

**Example:**
```kotlin
InputChip(
    id = "tag_${tag.id}",
    text = tag.name,
    trailingIcon = icon("close"),
    selected = true,
    events = {
        RemoveTiles(
            trigger = EventTriggers.onUncheck(),
            groupingTileId = "tagContainer",
            tileIds = listOf("tag_${tag.id}")
        )
    }
)
```

### SuggestionChip
**Purpose:** Renders a Material 3 SuggestionChip displaying dynamically generated suggestions.
**When to use:** Search suggestions, auto-complete options, possible replies.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.chips.SuggestionChip`

**Helper functions** (import same path as above):
- `defaultSuggestionChip()` → `SuggestionChipTileSchema.Variant.DEFAULT`
- `elevatedSuggestionChip()` → `SuggestionChipTileSchema.Variant.ELEVATED`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `text` | `String` | required | |
| `icon` | `IconSchema?` | `null` | |
| `enabled` | `Boolean` | `true` | Disabled chips do not fire clicks |
| `variant` | `SuggestionChipTileSchema.Variant` | `DEFAULT` | use `defaultSuggestionChip()` or `elevatedSuggestionChip()` |

**Updatable via UpdateTiles:** `style`, `visibility`, `text`, `icon`, `enabled`, `variant`
**Triggers dispatched:** `OnClick`

**Example:**
```kotlin
SuggestionChip(
    id = "suggestion_${suggestion.id}",
    text = suggestion.text,
    icon = icon("lightbulb"),
    events = {
        UpdateTiles(
            trigger = EventTriggers.onClick(),
            updates = {
                update(tileId = "searchBar", updateData = inlineTileUpdateData(mapOf("query" to suggestion.text)))
            }
        )
    }
)
```

---

## Inputs

### TextField
**Purpose:** Renders a Material 3 text input field (filled or outlined) with rich decoration and full keyboard configuration.
**When to use:** Any user text input — login forms, search, multi-line notes.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField`

**Helper functions** (import same path as above):
- `filledTextField()` → `TextFieldTileSchema.Kind.FILLED`
- `outlinedTextField()` → `TextFieldTileSchema.Kind.OUTLINED`
- `keyboardOptions(autoCorrectEnabled?, showKeyboardOnFocus?, capitalization, keyboardType, imeAction)` — builds `KeyboardOptions`
- **Capitalization:** `keyboardCapitalizationUnspecified()`, `keyboardCapitalizationNone()`, `keyboardCapitalizationCharacters()`, `keyboardCapitalizationWords()`, `keyboardCapitalizationSentences()`
- **KeyboardType:** `keyboardTypeUnspecified()`, `keyboardTypeText()`, `keyboardTypeAscii()`, `keyboardTypeNumber()`, `keyboardTypePhone()`, `keyboardTypeUri()`, `keyboardTypeEmail()`, `keyboardTypePassword()`, `keyboardTypeNumberPassword()`, `keyboardTypeDecimal()`
- **ImeAction:** `keyboardImeActionUnspecified()`, `keyboardImeActionDefault()`, `keyboardImeActionNone()`, `keyboardImeActionGo()`, `keyboardImeActionSearch()`, `keyboardImeActionSend()`, `keyboardImeActionPrevious()`, `keyboardImeActionNext()`, `keyboardImeActionDone()`
- **VisualTransformation:** `keyboardVisualTransformationNone()`, `keyboardVisualTransformationPassword()`, `keyboardVisualTransformationCustom(mask: String)`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `value` | `String` | `""` | Server-controlled current text |
| `enabled` | `Boolean` | `true` | |
| `leadingIcon` | `IconSchema?` | `null` | |
| `clickableLeadingIcon` | `Boolean` | `false` | Fires `OnLeadingIconClick` when true |
| `trailingIcon` | `IconSchema?` | `null` | |
| `clickableTrailingIcon` | `Boolean` | `true` | Fires `OnTrailingIconClick` when true |
| `prefixText` | `String?` | `null` | |
| `suffixText` | `String?` | `null` | |
| `placeholder` | `String?` | `null` | |
| `label` | `String?` | `null` | Floating label |
| `supportingText` | `String?` | `null` | Below-field helper or error text |
| `minLines` | `Int` | `1` | |
| `maxLines` | `Int` | `Int.MAX_VALUE` | |
| `kind` | `TextFieldTileSchema.Kind` | `OUTLINED` | use `filledTextField()` or `outlinedTextField()` |
| `state` | `TextFieldTileSchema.State` | `NORMAL` | NORMAL or ERROR |
| `keyboardOptions` | `TextFieldTileSchema.KeyboardOptions?` | `null` | use `keyboardOptions(...)` helper |
| `visualTransformation` | `TextFieldTileSchema.VisualTransformation?` | `null` | use `keyboardVisualTransformation*()` helpers |

**Updatable via UpdateTiles:** `value`, `enabled`, `leadingIcon`, `clickableLeadingIcon`, `trailingIcon`, `clickableTrailingIcon`, `prefixText`, `suffixText`, `placeholder`, `label`, `supportingText`, `minLines`, `maxLines`, `kind`, `state`, `keyboardOptions`, `visualTransformation`, `visibility`, `style`
**Triggers dispatched:** `OnTextChanged` (new text as incomingData), `OnKeyboardDone`, `OnKeyboardGo`, `OnKeyboardNext`, `OnKeyboardPrevious`, `OnKeyboardSearch`, `OnKeyboardSend`, `OnLeadingIconClick`, `OnTrailingIconClick`
**Notes:** The server must push `value` updates via UpdateTiles on each `onTextChanged` to keep the displayed text in sync. `state = ERROR` activates Material error styling.
**Form tile:** `produceValueWithKey` returns `null` when `value` is empty; otherwise `mapOf(key to value)` (`String`). Empty fields are excluded from the collected form data.

**Example:**
```kotlin
TextField(
    id = "email",
    placeholder = "E-mail",
    maxLines = 1,
    style = {
        size(width = fillHorizontally(max = 400))
        margin(horizontal = 24, bottom = 8)
    },
    events = {
        UpdateTiles(
            trigger = EventTriggers.onTextChanged(),
            updates = {
                update(
                    tileId = "email",
                    updateData = inlineTileUpdateData(
                        mapOf("state" to "NORMAL", "supportingText" to null, "trailingIcon" to null)
                    )
                )
            }
        )
    }
)
```

### Checkbox
**Purpose:** Renders a Material 3 checkbox whose checked state is fully server-controlled.
**When to use:** Boolean toggles, multi-select lists, terms-of-service acceptance.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.inputs.Checkbox`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `checked` | `Boolean` | `false` | Server-owned; must be updated via UpdateTiles |
| `enabled` | `Boolean` | `true` | |

**Updatable via UpdateTiles:** `checked`, `enabled`, `visibility`, `style`
**Triggers dispatched:** `OnCheck` (→ true), `OnUncheck` (→ false), `OnCheckChanged` (always)
**Notes:** Fully controlled component — the visual state snaps back to the server-provided `checked` value until UpdateTiles arrives.
**Form tile:** `produceValueWithKey` always returns `mapOf(key to tile.checked)` (`Boolean`). Always present — never null.

**Example:**
```kotlin
Checkbox(
    id = "termsCheckbox",
    checked = false,
    events = {
        UpdateTiles(
            trigger = EventTriggers.onCheck(),
            updates = {
                update(tileId = "termsCheckbox", updateData = inlineTileUpdateData(mapOf("checked" to true)))
                update(tileId = "submitButton", updateData = inlineTileUpdateData(mapOf("enabled" to true)))
            }
        )
        UpdateTiles(
            trigger = EventTriggers.onUncheck(),
            updates = {
                update(tileId = "termsCheckbox", updateData = inlineTileUpdateData(mapOf("checked" to false)))
                update(tileId = "submitButton", updateData = inlineTileUpdateData(mapOf("enabled" to false)))
            }
        )
    }
)
```

### RadioButton
**Purpose:** Renders a Material 3 radio button whose selected state is fully server-controlled. Multiple radio buttons with the same `groupId` form a logical group.
**When to use:** Single-choice selection from mutually exclusive options.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.inputs.RadioButton`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `selected` | `Boolean` | `false` | Server-owned |
| `enabled` | `Boolean` | `true` | |
| `groupId` | `String` | required | Tiles with same groupId form a selection group |

**Updatable via UpdateTiles:** `selected`, `enabled`, `visibility`, `style`
**Triggers dispatched:** `OnSelect`
**Notes:** On tap, the renderer dispatches a group-level event carrying this tile's `id` and `groupId`, allowing the server to auto-deselect other radio buttons in the group. The `selected` field is still server-owned.
**Form tile:** `produceValueWithKey` always returns `mapOf(key to tile.selected)` (`Boolean`). Always present — never null. Typically only the selected button in a group carries `true`.

**Example:**
```kotlin
listOf("daily", "weekly", "monthly").forEachIndexed { i, freq ->
    RadioButton(
        id = "freq_$freq",
        groupId = "frequency_group",
        selected = i == 0,
        events = {
            UpdateTiles(
                trigger = EventTriggers.onSelect(),
                updates = {
                    listOf("daily", "weekly", "monthly").forEach { f ->
                        update(
                            tileId = "freq_$f",
                            updateData = inlineTileUpdateData(mapOf("selected" to (f == freq)))
                        )
                    }
                }
            )
        }
    )
}
```

### Switch
**Purpose:** Renders a Material 3 switch (toggle) whose on/off state is fully server-controlled.
**When to use:** On/off settings, feature toggles, notification preferences.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.inputs.Switch`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `checked` | `Boolean` | `false` | Server-owned |
| `enabled` | `Boolean` | `true` | |

**Updatable via UpdateTiles:** `checked`, `enabled`, `visibility`, `style`
**Triggers dispatched:** `OnCheck`, `OnUncheck`, `OnCheckChanged`
**Notes:** Identical trigger semantics to Checkbox. Fully controlled component.
**Form tile:** `produceValueWithKey` always returns `mapOf(key to tile.checked)` (`Boolean`). Always present — never null.

**Example:**
```kotlin
Switch(
    id = "notificationsSwitch",
    checked = user.notificationsEnabled,
    events = {
        UpdateData(
            trigger = EventTriggers.onCheck(),
            updates = {
                update(dataSource = screenPlainData(), updateData = inlineUpdateData("notifications_enabled" to true))
            }
        )
        UpdateData(
            trigger = EventTriggers.onUncheck(),
            updates = {
                update(dataSource = screenPlainData(), updateData = inlineUpdateData("notifications_enabled" to false))
            }
        )
    }
)
```

---

## Image & Icon

### Icon
**Purpose:** Renders a single icon using the Mosaic Icon composable, resolving the icon from the provided `IconSchema`.
**When to use:** Decorative or informational icons that are not interactive. For interactive icons, wrap in IconButton or Button.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.image.Icon`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `icon` | `IconSchema` | required | Use `icon()`, `outlinedIcon()`, `roundedIcon()` helpers |

**Updatable via UpdateTiles:** `style`, `visibility`, `icon`
**Triggers dispatched:** None.

**Example:**
```kotlin
Icon(
    id = "statusIcon",
    icon = icon("check_circle"),
    style = { size(width = fixedHorizontally(24), height = fixedVertically(24)) }
)
```

### AsyncImage
**Purpose:** Renders a remotely loaded image using Coil 3's `AsyncImage` composable.
**When to use:** Any image loaded from a URL (avatars, product photos, banners).
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.image.AsyncImage`

**Helper functions** (import same path as above):
- `cropContentScale()` → `AsyncImageTileSchema.ContentScale.CROP`
- `fitContentScale()` → `AsyncImageTileSchema.ContentScale.FIT`
- `fillHeightContentScale()` → `AsyncImageTileSchema.ContentScale.FILL_HEIGHT`
- `fillWidthContentScale()` → `AsyncImageTileSchema.ContentScale.FILL_WIDTH`
- `insideContentScale()` → `AsyncImageTileSchema.ContentScale.INSIDE`
- `fillBoundsContentScale()` → `AsyncImageTileSchema.ContentScale.FILL_BOUNDS`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `url` | `String` | required | Remote image URL |
| `contentDescription` | `String?` | `null` | Accessibility |
| `contentScale` | `AsyncImageTileSchema.ContentScale` | `FIT` | use `cropContentScale()`, `fitContentScale()`, etc. |
| `alpha` | `Float` | `1.0f` | |
| `clipToBounds` | `Boolean` | `true` | |
| `alignment` | `AlignmentSchema.TwoDimensional` | `Center` | |

**Updatable via UpdateTiles:** `style`, `visibility`, `url`, `contentDescription`, `contentScale`, `alpha`, `clipToBounds`, `alignment`
**Triggers dispatched:** `OnAsyncImageLoadStart`, `OnAsyncImageLoadSuccess`, `OnAsyncImageLoadFailure`

**Example:**
```kotlin
AsyncImage(
    id = "avatar",
    url = user.avatarUrl,
    contentScale = cropContentScale(),
    contentDescription = "${user.name} avatar",
    style = {
        size(width = fixedHorizontally(48), height = fixedVertically(48))
        clip { roundedCornerClip(24) }
    }
)
```

### Image
**Purpose:** Renders a static image from a bundled drawable resource identified by `resourceName`. The resource is resolved via `DrawableResourcesHolder`.
**When to use:** App-bundled images (splash screens, logos, illustrations) registered in the client's `DrawableResourcesHolder`.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.image.Image`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `resourceName` | `String` | required | Key registered in client `DrawableResourcesHolder` |
| `contentDescription` | `String?` | `null` | |
| `contentScale` | `ImageTileSchema.ContentScale` | `FIT` | CROP, FIT, FILL_HEIGHT, FILL_WIDTH, INSIDE, FILL_BOUNDS |
| `alpha` | `Float` | `1.0f` | |
| `alignment` | `AlignmentSchema.TwoDimensional` | `Center` | |

**Updatable via UpdateTiles:** `style`, `visibility`, `resourceName`, `contentDescription`, `contentScale`, `alpha`, `alignment`
**Triggers dispatched:** None.
**Notes:** Requesting an unknown `resourceName` silently renders nothing. The server can only reference resources the client has registered.

**Example:**
```kotlin
Image(
    id = "logo",
    resourceName = "app_logo",
    contentScale = ImageTileSchema.ContentScale.FIT,
    contentDescription = "App logo"
)
```

---

## Grouping / Layout

### Column
**Purpose:** Renders a vertical column that stacks its child tiles from top to bottom using Compose's `Column` layout. Supports optional scrolling and child filtering.
**When to use:** Any vertical stack of tiles; use `scrollable = true` for scrollable forms/lists when items are pre-known. Use `filterChildrenByTerm` to implement search filtering.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `arrangement` | `ArrangementSchema.Vertical` | `arrangeVerticallyToTop()` | |
| `alignment` | `AlignmentSchema.Horizontal` | `alignHorizontallyToStart()` | |
| `scrollable` | `Boolean` | `false` | Attaches vertical scroll state |
| `filterChildrenByTerm` | `String?` | `null` | Filters children by their `searchableTerms`; null = show all |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | Child tiles |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `arrangement`, `alignment`, `scrollable`, `searchableTerms`, `filterChildrenByTerm`
**Triggers dispatched:** `OnDisplay`, `OnScrolled` (with direction), `OnClick`, `OnLongPress`
**Notes:** Exposes `LocalColumnScope` so children can use `ColumnScope` modifiers (e.g. `weight`). Scroll broadcast commands shared with `LazyColumn`: `ScrollToTop`, `ScrollTo(index)`, `ScrollToBottom`. Children with `searchableTerms = null` are always shown regardless of `filterChildrenByTerm`.

**Example:**
```kotlin
Column(
    id = "environmentList",
    filterChildrenByTerm = null, // updated via UpdateTiles when search query changes
    arrangement = arrangeVerticallyToTop(),
    style = { size(width = fillHorizontally(), height = fillVertically()) }
) {
    environments.forEach { env -> EnvironmentTile(env) }
}
```

### LazyColumn
**Purpose:** Renders a vertically scrolling lazy list that only composes visible items using Compose's `LazyColumn`. Each child tile is keyed by its `id` for stable recomposition.
**When to use:** Long or dynamic lists where items are not all known upfront; use `scrollThreshold` for pagination.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyColumn`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `arrangement` | `ArrangementSchema.Vertical` | `arrangeVerticallyToTop()` | |
| `alignment` | `AlignmentSchema.Horizontal` | `alignHorizontallyToStart()` | |
| `scrollThreshold` | `Int?` | `null` | Items from end to fire `OnScrollThresholdReached` |
| `considerLoadingItemAtEndOnThresholdReached` | `Boolean` | `true` | Accounts for a loading placeholder at end |
| `filterChildrenByTerm` | `String?` | `null` | Filters children by `searchableTerms` |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `arrangement`, `alignment`, `scrollThreshold`, `considerLoadingItemAtEndOnThresholdReached`, `searchableTerms`, `filterChildrenByTerm`
**Triggers dispatched:** `OnDisplay`, `OnScrolled`, `OnScrollThresholdReached` (when `scrollThreshold` non-null), `OnClick`, `OnLongPress`
**Notes:** Exposes `LocalLazyColumnRenderingScope`. Uses same broadcast channel as `Column`. `considerLoadingItemAtEndOnThresholdReached = true` accounts for a loading placeholder appended at end of list.

**Example:**
```kotlin
LazyColumn(
    id = "itemList",
    scrollThreshold = 3,
    style = { size(width = fillHorizontally(), height = fillVertically()) },
    events = {
        AddTiles(
            trigger = EventTriggers.onScrollThresholdReached(),
            groupingTileId = "itemList",
            position = insertAtEnd(),
            tiles = { /* next page items */ }
        )
    }
) {
    items.forEach { item -> ItemCard(item) }
}
```

### Row
**Purpose:** Renders a horizontal row that places child tiles side by side using Compose's `Row` layout.
**When to use:** Horizontal layouts — action bars, icon+label pairs, form field rows.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `arrangement` | `ArrangementSchema.Horizontal` | `arrangeHorizontallyToStart()` | |
| `alignment` | `AlignmentSchema.Vertical` | `alignVerticallyToTop()` | |
| `scrollable` | `Boolean` | `false` | |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `arrangement`, `alignment`, `scrollable`
**Triggers dispatched:** `OnDisplay`, `OnScrolled` (direction Start/End), `OnClick`, `OnLongPress`
**Notes:** Exposes `LocalRowScope`. Broadcast channel shared with `LazyRow`: `ScrollToStart`, `ScrollTo(index)`, `ScrollToEnd`.

**Example:**
```kotlin
Row(
    id = "actionBar",
    arrangement = arrangeHorizontallyWithSpaceBetween(),
    alignment = alignVerticallyCentered()
) {
    SimpleText(text = "Title")
    IconButton(
        id = "searchBtn",
        icon = icon("search"),
        events = {
            Navigate(trigger = EventTriggers.onClick(), screenId = "search")
        }
    )
}
```

### LazyRow
**Purpose:** Renders a horizontally scrolling lazy list using Compose's `LazyRow`. Only visible items are composed.
**When to use:** Horizontal scrolling lists — carousels of text-based items, category chips, thumbnail strips.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyRow`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `arrangement` | `ArrangementSchema.Horizontal` | `arrangeHorizontallyToStart()` | |
| `alignment` | `AlignmentSchema.Vertical` | `alignVerticallyToTop()` | |
| `scrollThreshold` | `Int?` | `null` | Items from end to fire `OnScrollThresholdReached` |
| `considerLoadingItemAtEndOnThresholdReached` | `Boolean` | `true` | |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `arrangement`, `alignment`, `scrollThreshold`, `considerLoadingItemAtEndOnThresholdReached`
**Triggers dispatched:** `OnDisplay`, `OnScrolled` (direction End/Start), `OnScrollThresholdReached`, `OnClick`, `OnLongPress`

**Example:**
```kotlin
LazyRow(
    id = "categoryRow",
    arrangement = arrangeHorizontallyWithSpacing(8)
) {
    categories.forEach { cat ->
        FilterChip(
            id = "cat_${cat.id}",
            text = cat.name,
            selected = cat.isSelected,
            events = {
                UpdateTiles(
                    trigger = EventTriggers.onCheck(),
                    updates = {
                        update(tileId = "cat_${cat.id}", updateData = inlineTileUpdateData(mapOf("selected" to true)))
                    }
                )
                UpdateTiles(
                    trigger = EventTriggers.onUncheck(),
                    updates = {
                        update(tileId = "cat_${cat.id}", updateData = inlineTileUpdateData(mapOf("selected" to false)))
                    }
                )
            }
        )
    }
}
```

### Box
**Purpose:** Renders a stacking container that overlays child tiles on top of each other using Compose's `Box` layout.
**When to use:** Overlapping content — badge over icon, overlay buttons on image, centered loading indicator.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `alignment` | `AlignmentSchema.TwoDimensional` | `alignToTopStart()` | Default alignment for children |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | `{}` | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `alignment`
**Triggers dispatched:** `OnDisplay`, `OnClick`, `OnLongPress`
**Notes:** Does not support scrolling or expose a scoped CompositionLocal.

**Example:**
```kotlin
Box(
    id = "avatarBox",
    alignment = alignToCenter()
) {
    AsyncImage(id = "avatar", url = user.avatarUrl, style = { size(fixedHorizontally(48), fixedVertically(48)) })
    Badge(id = "onlineBadge", content = null)
}
```

### Card
**Purpose:** Renders a Material 3 card container that groups child tiles inside a surface with elevation, shape, and color from the Material theme.
**When to use:** Grouping related content with visual elevation — list items, product cards, info panels.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `kind` | `CardTileSchema.Kind` | `DEFAULT` | DEFAULT (filled), ELEVATED (shadow), OUTLINED (border) |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `kind`
**Triggers dispatched:** `OnDisplay`, `OnClick` (always wired, even if no events registered)
**Notes:** Uses `ColumnScope` internally and exposes `LocalColumnScope`. Click handler is always wired — child events must be registered to observe clicks.

**Example:**
```kotlin
Card(
    id = "env_card_${env.id}",
    kind = CardTileSchema.Kind.OUTLINED,
    events = {
        Navigate(trigger = EventTriggers.onClick(), screenId = "env_detail", params = mapOf("id" to env.id))
    }
) {
    SimpleText(text = env.name, typography = titleMedium())
    SimpleText(text = env.url, color = onSurfaceVariantColor())
}
```

### Carousel
**Purpose:** Renders a Material 3 horizontal carousel displaying child tiles as browsable cards. Two variants via `type`: `MultiBrowse` (multiple visible items with peeking) or `Uncontained` (fixed item width).
**When to use:** Horizontally browsable cards with visual variation — feature highlights, media galleries, product showcases.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.Carousel`

**Helper functions** (import same path as above):
- `multiBrowse(preferredItemWidth: Int, minSmallItemWidth: Int? = null, maxSmallItemWidth: Int? = null)` → `CarouselTypeSchema.MultiBrowse` — shows multiple items with peeking
- `uncontained(itemWidth: Int)` → `CarouselTypeSchema.Uncontained` — fixed item width, no constraints on partial items

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `type` | `CarouselTypeSchema` | required | use `multiBrowse(...)` or `uncontained(...)` |
| `itemSpacing` | `Int` | `0` | dp spacing between items |
| `contentPadding` | `Int` | `0` | Symmetric horizontal padding in dp |
| `userScrollEnabled` | `Boolean` | `true` | Disable for programmatic-only scrolling |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `type`, `itemSpacing`, `contentPadding`, `userScrollEnabled`
**Triggers dispatched:** `OnDisplay`
**Notes:** Item count is derived from `tiles.size` at render time. `contentPadding` is applied as symmetric horizontal padding.

**Example:**
```kotlin
Carousel(
    id = "featuredCarousel",
    type = multiBrowse(preferredItemWidth = 240, minSmallItemWidth = 40, maxSmallItemWidth = 80),
    itemSpacing = 8,
    contentPadding = 16
) {
    featured.forEach { item ->
        Card(id = "featured_${item.id}", kind = CardTileSchema.Kind.ELEVATED) {
            AsyncImage(id = "img_${item.id}", url = item.imageUrl, style = { size(fillHorizontally(), fixedVertically(140)) })
            SimpleText(text = item.title, typography = titleSmall())
        }
    }
}
```

### Pager
**Purpose:** Renders a horizontal swipeable pager where each child tile occupies one page using Compose's `HorizontalPager`.
**When to use:** Onboarding flows, tabbed content views, image galleries, step-by-step wizards.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.Pager`

**Helper functions** (import same path as above):
- `pageFill()` → `PageSizeSchema.Fill` — page fills the viewport width
- `pageFixed(value: Int)` → `PageSizeSchema.Fixed(value)` — fixed dp width

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `pageSize` | `PageSizeSchema` | `Fill` | use `pageFill()` or `pageFixed(dp)` |
| `pageSpacing` | `Int` | `0` | dp spacing between pages |
| `contentPadding` | `Int` | `0` | |
| `beyondViewportPageCount` | `Int` | `0` | Pre-compose extra pages |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `pageSize`, `pageSpacing`, `contentPadding`, `beyondViewportPageCount`
**Triggers dispatched:** `OnDisplay`, `OnPageChanged` — fires with `Direction.Any`, `Direction.Start` (page 0), `Direction.End` (last page), `Direction.Index(page)` on every change after initial render.
**Notes:** Initial page change (from page 0) is suppressed to avoid spurious triggers on composition.

**Example:**
```kotlin
Pager(
    id = "onboardingPager",
    pageSize = pageFill(),
    events = {
        UpdateTiles(
            trigger = EventTriggers.onPageChanged(),
            updates = {
                update(tileId = "pageIndicator", updateData = incomingTileUpdateData())
            }
        )
    }
) {
    onboardingPages.forEach { page ->
        Column(id = "page_${page.index}") {
            Image(id = "page_img_${page.index}", resourceName = page.imageName)
            SimpleText(text = page.title, typography = headlineMedium())
        }
    }
}
```

### Grid
**Purpose:** Renders a CSS-grid-like two-dimensional layout using the experimental Compose `Grid` API. Columns and rows are defined as typed track lists.
**When to use:** Two-dimensional layouts — image grids, dashboard tiles, complex form layouts.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.Grid`

**Helper functions** (import same path as above):
- `flowGridThroughRows()` → `GridFlowSchema.Row` — items fill row-first
- `flowGridThroughColumns()` → `GridFlowSchema.Column` — items fill column-first
- **Column tracks:** `gridColumnFixed(dp)`, `gridColumnFraction(f)`, `gridColumnFlexible(f)`, `gridColumnAuto()`, `gridColumnMaxContent()`, `gridColumnMinContent()`
- **Row tracks:** `gridRowFixed(dp)`, `gridRowFraction(f)`, `gridRowFlexible(f)`, `gridRowAuto()`, `gridRowMaxContent()`, `gridRowMinContent()`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `columns` | `List<GridTrackSchema>` | required | use `gridColumnFixed()`, `gridColumnFraction()`, etc. |
| `rows` | `List<GridTrackSchema>` | `[]` | use `gridRowFixed()`, `gridRowFraction()`, etc. |
| `columnGap` | `Int` | `0` | dp |
| `rowGap` | `Int` | `0` | dp |
| `flow` | `GridFlowSchema` | `Row` | use `flowGridThroughRows()` or `flowGridThroughColumns()` |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `columns`, `rows`, `columnGap`, `rowGap`, `flow`
**Triggers dispatched:** `OnDisplay`, `OnClick`
**Notes:** Children are composed eagerly (not lazy). Exposes `LocalGridScope` for `columnSpan`/`rowSpan` modifiers.

**Example:**
```kotlin
Grid(
    id = "dashboardGrid",
    columns = listOf(gridColumnFraction(0.5f), gridColumnFraction(0.5f)),
    rows = emptyList(),
    columnGap = 8,
    rowGap = 8
) {
    metrics.forEach { m ->
        Card(id = "metric_${m.id}") { SimpleText(text = m.label) }
    }
}
```

### FlexBox
**Purpose:** Renders a CSS flexbox-style container using the experimental Compose `FlexBox` API.
**When to use:** Flexible layouts that need direction control, justify/align, and optional wrapping — similar to web flexbox.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlexBox`

**Helper functions** (import same path as above):
- **Direction:** `flexDirectionRow()`, `flexDirectionRowReverse()`, `flexDirectionColumn()`, `flexDirectionColumnReverse()`
- **JustifyContent:** `flexJustifyStart()`, `flexJustifyCenter()`, `flexJustifyEnd()`, `flexJustifySpaceBetween()`, `flexJustifySpaceAround()`, `flexJustifySpaceEvenly()`
- **AlignItems:** `flexAlignItemsStart()`, `flexAlignItemsCenter()`, `flexAlignItemsEnd()`, `flexAlignItemsStretch()`, `flexAlignItemsBaseline()`
- **AlignContent:** `flexAlignContentStart()`, `flexAlignContentCenter()`, `flexAlignContentEnd()`, `flexAlignContentStretch()`, `flexAlignContentSpaceBetween()`, `flexAlignContentSpaceAround()`
- **Wrap:** `flexNoWrap()`, `flexWrap()`, `flexWrapReverse()`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `direction` | `FlexDirectionSchema` | `Row` | use `flexDirectionRow()`, `flexDirectionColumn()`, etc. |
| `justifyContent` | `FlexJustifyContentSchema` | `Start` | use `flexJustifyStart()`, `flexJustifyCenter()`, etc. |
| `alignItems` | `FlexAlignItemsSchema` | `Start` | use `flexAlignItemsStart()`, `flexAlignItemsCenter()`, etc. |
| `alignContent` | `FlexAlignContentSchema` | `Start` | use `flexAlignContentStart()`, `flexAlignContentStretch()`, etc. |
| `wrap` | `FlexWrapSchema` | `NoWrap` | use `flexNoWrap()`, `flexWrap()`, `flexWrapReverse()` |
| `columnGap` | `Int` | `0` | dp |
| `rowGap` | `Int` | `0` | dp |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `direction`, `justifyContent`, `alignItems`, `alignContent`, `wrap`, `columnGap`, `rowGap`
**Triggers dispatched:** `OnDisplay`, `OnClick`
**Notes:** Exposes `LocalFlexBoxScope` for `flexGrow`, `flexShrink`, `alignSelf` modifiers. Children composed eagerly.

**Example:**
```kotlin
FlexBox(
    id = "tagCloud",
    direction = flexDirectionRow(),
    wrap = flexWrap(),
    columnGap = 8,
    rowGap = 8,
    justifyContent = flexJustifyStart()
) {
    tags.forEach { tag -> AssistChip(id = "tag_${tag.id}", text = tag.name) }
}
```

### FlowRow
**Purpose:** Renders a horizontally-wrapping flow layout using Compose's `FlowRow`. Children wrap onto new rows when exceeding available width.
**When to use:** Chip groups, tag clouds, badge collections — any set of variable-width items that should wrap naturally.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlowRow`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `horizontalArrangement` | `ArrangementSchema.Horizontal` | `arrangeHorizontallyToStart()` | |
| `verticalArrangement` | `ArrangementSchema.Vertical` | `arrangeVerticallyToTop()` | |
| `maxItemsInEachRow` | `Int` | `Int.MAX_VALUE` | Cap items per row |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `horizontalArrangement`, `verticalArrangement`, `maxItemsInEachRow`
**Triggers dispatched:** `OnDisplay`, `OnClick`
**Notes:** Exposes `LocalFlowRowScope` for `fillMaxRowHeight`/`weight` modifiers.

**Example:**
```kotlin
FlowRow(
    id = "filterChips",
    horizontalArrangement = arrangeHorizontallyWithSpacing(8),
    verticalArrangement = arrangeVerticallyWithSpacing(8)
) {
    filters.forEach { f ->
        FilterChip(
            id = "filter_${f.id}",
            text = f.label,
            selected = f.isActive,
            events = {
                UpdateTiles(
                    trigger = EventTriggers.onCheck(),
                    updates = {
                        update(tileId = "filter_${f.id}", updateData = inlineTileUpdateData(mapOf("selected" to true)))
                    }
                )
                UpdateTiles(
                    trigger = EventTriggers.onUncheck(),
                    updates = {
                        update(tileId = "filter_${f.id}", updateData = inlineTileUpdateData(mapOf("selected" to false)))
                    }
                )
            }
        )
    }
}
```

### Shimmer
**Purpose:** Renders a shimmer loading effect over child tiles using the `compose-shimmer` library. The animated highlight sweep covers the entire subtree simultaneously.
**When to use:** Skeleton loading screens — while network data is being fetched, show shimmer over placeholder shapes.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.Shimmer`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | Placeholder shapes to shimmer over |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`
**Triggers dispatched:** `OnDisplay`
**Notes:** Shimmer is always active while visible. To stop it, replace or hide via UpdateTiles/ReplaceTiles.

**Example:**
```kotlin
Shimmer(id = "listShimmer") {
    repeat(5) { i ->
        Row(id = "shimmer_row_$i", style = { padding { all(12) } }) {
            Box(id = "shimmer_avatar_$i", style = { size(fixedHorizontally(40), fixedVertically(40)); clip { circleClip() } }) {}
            Column(id = "shimmer_text_$i") {
                Box(id = "shimmer_title_$i", style = { size(fixedHorizontally(120), fixedVertically(16)) }) {}
                Box(id = "shimmer_sub_$i", style = { size(fixedHorizontally(80), fixedVertically(12)) }) {}
            }
        }
    }
}
```

### PullToRefresh
**Purpose:** Renders a Material 3 pull-to-refresh container (`PullToRefreshBox`) that wraps child tiles. The spinning indicator remains visible while `isRefreshing` is `true`.
**When to use:** Lists or feeds that support manual refresh by the user pulling down.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.PullToRefresh`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `isRefreshing` | `Boolean` | required | Server-controlled; must be set to `false` when refresh completes |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `isRefreshing`
**Triggers dispatched:** `OnDisplay`, `OnPull` — fired when the user completes the pull gesture.
**Notes:** The server must toggle `isRefreshing` to `false` via UpdateTiles once the refresh operation completes; otherwise the spinner remains indefinitely.

**Example:**
```kotlin
PullToRefresh(
    id = "feedRefresh",
    isRefreshing = false,
    tiles = {
        LazyColumn(id = "feedList") { items.forEach { FeedItem(it) } }
    },
    events = {
        UpdateTiles(
            trigger = EventTriggers.onPull(),
            updates = {
                update(tileId = "feedRefresh", updateData = inlineTileUpdateData(mapOf("isRefreshing" to true)))
            }
        )
        SendNetworkRequest(
            trigger = EventTriggers.onPull(),
            networkRequestData = feedNetworkRequestData,
            events = {
                UpdateTiles(
                    trigger = EventTriggers.onSuccess(),
                    updates = {
                        update(tileId = "feedRefresh", updateData = inlineTileUpdateData(mapOf("isRefreshing" to false)))
                        update(tileId = "feedList", updateData = incomingTileUpdateData())
                    }
                )
                UpdateTiles(
                    trigger = EventTriggers.onFailure(),
                    updates = {
                        update(tileId = "feedRefresh", updateData = inlineTileUpdateData(mapOf("isRefreshing" to false)))
                    }
                )
            }
        )
    }
)
```

### AdaptiveVisibility
**Purpose:** Renders child tiles only when the current window size satisfies the configured breakpoint constraints on both width and height axes.
**When to use:** Responsive layouts — show a sidebar only on expanded width, hide a details panel on compact height.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.AdaptiveVisibility`

**Helper functions** (import same path as above):
- **Width (VisibleFrom):** `widthVisibleFromCompact()`, `widthVisibleFromMedium()`, `widthVisibleFromExpanded()`, `widthVisibleFromLarge()`, `widthVisibleFromExtraLarge()`
- **Width (VisibleUntil):** `widthVisibleUntilCompact()`, `widthVisibleUntilMedium()`, `widthVisibleUntilExpanded()`, `widthVisibleUntilLarge()`, `widthVisibleUntilExtraLarge()`
- **Height (VisibleFrom):** `heightVisibleFromCompact()`, `heightVisibleFromMedium()`, `heightVisibleFromExpanded()`
- **Height (VisibleUntil):** `heightVisibleUntilCompact()`, `heightVisibleUntilMedium()`, `heightVisibleUntilExpanded()`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `widthVisibility` | `WidthVisibility` | `widthVisibleUntilExtraLarge()` | Visible at all widths by default |
| `heightVisibility` | `HeightVisibility` | `heightVisibleUntilExpanded()` | Visible at all heights by default |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `widthVisibility`, `heightVisibility`
**Triggers dispatched:** `OnDisplay`, `OnWidthBreakpointSatisfied`, `OnWidthBreakpointNotSatisfied`, `OnHeightBreakpointSatisfied`, `OnHeightBreakpointNotSatisfied`
**Notes:** Transparent logical container — does not create its own layout node. `style` is not applied; children participate directly in the parent layout.

**Example:**
```kotlin
AdaptiveVisibility(
    id = "sidePanel",
    widthVisibility = widthVisibleFromMedium(),
    heightVisibility = heightVisibleUntilExpanded()
) {
    Column(id = "sidePanelContent") { /* detail panel */ }
}
```

### LazyTiles
**Purpose:** Renders a self-loading tile container that fetches child tiles from a remote endpoint on first composition. Shows placeholders while loading and failure tiles on error.
**When to use:** Server-delegated section loading — a screen section whose tiles come from a separate endpoint (e.g. a dashboard widget loaded independently).
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.grouping.LazyTiles`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `url` | `String` | required | Endpoint returning `List<TileSchema>` JSON |
| `method` | `HttpMethod` | `GET` | HTTP method |
| `body` | `AnySerializable?` | `null` | Request body |
| `headers` | `Map<String, String>?` | `null` | Request headers |
| `placeholderTiles` | `TileSchemaBuilderScope.() -> Unit` | `{}` | Shown while loading |
| `failureTiles` | `TileSchemaBuilderScope.() -> Unit` | `{}` | Shown on error |

**Updatable via UpdateTiles:** `tiles`, `style`, `visibility`, `isFailureState`, `placeholderTiles`, `failureTiles`, `url`, `method`, `body`, `headers`
**Triggers dispatched:** `OnDisplay`, `OnLoadTilesStart`, `OnLoadTilesSuccess`, `OnLoadTilesFailure`
**Notes:** Network call via `SingleEffect` (one-shot). If `tiles` is already non-null (e.g. via UpdateTiles), the fetch is skipped. Use `ReloadLazyTiles` event to force re-fetch.

**Example:**
```kotlin
LazyTiles(
    id = "recommendationsSection",
    url = "/api/recommendations",
    placeholderTiles = { Shimmer(id = "recShimmer") { /* placeholder shapes */ } },
    failureTiles = { SimpleText(text = "Failed to load recommendations") }
)
```

---

## Menu

### Menu
**Purpose:** Renders a `Box` that overlays a `DropdownMenu` on top of its `tiles` children (the anchor). The dropdown is shown/hidden based on `expanded`.
**When to use:** Contextual action menus triggered by an icon button or long-press, dropdown selectors.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.menu.Menu`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `expanded` | `Boolean` | `false` | Server-controlled open/close state |
| `items` | `MenuItemSchemaBuilderScope.() -> Unit` | required | Use `addMenuItem(id, label, leadingIcon, trailingIcon)` |
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required | Anchor content (e.g. icon button) |

**MenuItem fields:** `id: String`, `label: String`, `leadingIcon: IconSchema?`, `trailingIcon: IconSchema?`

**Updatable via UpdateTiles:** `style`, `visibility`, `tiles`, `items`, `expanded`
**Triggers dispatched:** `OnMenuItemClick` — carries the clicked item's `id` as trigger parameter.
**Notes:** The server must set `expanded = false` via UpdateTiles when dismissal is requested. When the user taps outside, the renderer dispatches a toggle event requesting a server update.

**Example:**
```kotlin
Menu(
    id = "actionsMenu",
    expanded = false,
    items = {
        addMenuItem(id = "edit", label = "Edit", leadingIcon = icon("edit"))
        addMenuItem(id = "delete", label = "Delete", leadingIcon = icon("delete"))
    },
    tiles = {
        IconButton(
            id = "menuTrigger",
            icon = icon("more_vert"),
            events = {
                UpdateTiles(
                    trigger = EventTriggers.onClick(),
                    updates = {
                        update(tileId = "actionsMenu", updateData = inlineTileUpdateData(mapOf("expanded" to true)))
                    }
                )
            }
        )
    },
    events = {
        Navigate(
            trigger = EventTriggers.onMenuItemClick(itemId = "edit"),
            screenId = "edit_screen"
        )
        UpdateTiles(
            trigger = EventTriggers.onMenuItemClick(itemId = "delete"),
            updates = {
                update(tileId = "actionsMenu", updateData = inlineTileUpdateData(mapOf("expanded" to false)))
            }
        )
        SendNetworkRequest(
            trigger = EventTriggers.onMenuItemClick(itemId = "delete"),
            networkRequestData = deleteNetworkRequestData
        )
    }
)
```

---

## Navigation

### NavigationBar
**Purpose:** Renders a Material 3 `NavigationBar` with a fixed set of destination items. Each item shows an icon (filled when selected) and an optional label.
**When to use:** Bottom navigation with 2–5 top-level destinations.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.navigation.NavigationBar`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `selectedItemId` | `String` | required | Server-controlled selected item |
| `items` | `NavigationBarItemSchemaBuilderScope.() -> Unit` | required | Use `item(id, icon, label)` |

**Item fields:** `id: String`, `icon: IconSchema`, `label: String?`

**Updatable via UpdateTiles:** `style`, `visibility`, `items`, `selectedItemId`
**Triggers dispatched:** `OnNavigationBarItemClick` — carries clicked item's `id`.
**Notes:** Selected state is entirely server-driven. Icon filling (filled vs. outlined) is determined by comparing each item's `id` against `selectedItemId`.

**Example:**
```kotlin
NavigationBar(
    id = "bottomNav",
    selectedItemId = "home",
    items = {
        item(id = "home", icon = icon("home"), label = "Home")
        item(id = "search", icon = icon("search"), label = "Search")
        item(id = "profile", icon = icon("person"), label = "Profile")
    },
    events = {
        Navigate(trigger = EventTriggers.onNavigationBarItemClick(itemId = "home"), screenId = "home")
        Navigate(trigger = EventTriggers.onNavigationBarItemClick(itemId = "search"), screenId = "search")
        Navigate(trigger = EventTriggers.onNavigationBarItemClick(itemId = "profile"), screenId = "profile")
    }
)
```

### NavigationRail
**Purpose:** Renders a Material 3 `NavigationRail` with a vertical list of destination items. Supports optional header (top) and footer (bottom) tiles.
**When to use:** Medium+ width layouts where a navigation rail is preferred over a bottom bar.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.navigation.NavigationRail`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `selectedItemId` | `String` | required | |
| `header` | `(TileSchemaBuilderScope.() -> Unit)?` | `null` | Optional tile at top of rail |
| `footer` | `(TileSchemaBuilderScope.() -> Unit)?` | `null` | Optional tile pushed to bottom |
| `items` | `NavigationRailItemSchemaBuilderScope.() -> Unit` | required | Use `addItem(id, icon, label)` |

**Updatable via UpdateTiles:** `style`, `visibility`, `items`, `selectedItemId`, `header`, `footer`
**Triggers dispatched:** `OnNavigationRailItemClick` — carries clicked item's `id`.

**Example:**
```kotlin
NavigationRail(
    id = "sideNav",
    selectedItemId = currentDestination,
    header = {
        FloatingActionButton(
            id = "railFab",
            icon = icon("add"),
            events = {
                Navigate(trigger = EventTriggers.onClick(), screenId = "create")
            }
        )
    },
    items = {
        addItem(id = "home", icon = icon("home"), label = "Home")
        addItem(id = "library", icon = icon("library_books"), label = "Library")
    },
    events = {
        Navigate(trigger = EventTriggers.onNavigationRailItemClick(itemId = "home"), screenId = "home")
        Navigate(trigger = EventTriggers.onNavigationRailItemClick(itemId = "library"), screenId = "library")
    }
)
```

### Tabs
**Purpose:** Renders a Material 3 tab row in one of four variants based on `tabType` and `scrollable`.
**When to use:** Secondary navigation within a screen — content categories, view mode switchers.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.navigation.Tabs`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `selectedTabId` | `String` | required | Server-controlled |
| `tabType` | `TabsTileSchema.Type` | `PRIMARY` | PRIMARY or SECONDARY |
| `scrollable` | `Boolean` | `false` | |
| `tabItems` | `TabItemSchemaBuilderScope.() -> Unit` | required | Use `addTab(id, label, icon, badgeText)` |

**Variants:** PRIMARY + non-scrollable → `PrimaryTabRow`; PRIMARY + scrollable → `PrimaryScrollableTabRow`; SECONDARY variants analogous.

**Updatable via UpdateTiles:** `style`, `visibility`, `selectedTabId`, `tabItems`, `tabType`, `scrollable`
**Triggers dispatched:** `OnTabItemClick` — carries clicked tab's `id`.

**Example:**
```kotlin
Tabs(
    id = "contentTabs",
    selectedTabId = "all",
    tabType = TabsTileSchema.Type.PRIMARY,
    scrollable = false,
    tabItems = {
        addTab(id = "all", label = "All")
        addTab(id = "active", label = "Active")
        addTab(id = "archived", label = "Archived")
    },
    events = {
        UpdateTiles(
            trigger = EventTriggers.onTabItemClick(itemId = "all"),
            updates = {
                update(tileId = "contentTabs", updateData = inlineTileUpdateData(mapOf("selectedTabId" to "all")))
            }
        )
        UpdateTiles(
            trigger = EventTriggers.onTabItemClick(itemId = "active"),
            updates = {
                update(tileId = "contentTabs", updateData = inlineTileUpdateData(mapOf("selectedTabId" to "active")))
            }
        )
        UpdateTiles(
            trigger = EventTriggers.onTabItemClick(itemId = "archived"),
            updates = {
                update(tileId = "contentTabs", updateData = inlineTileUpdateData(mapOf("selectedTabId" to "archived")))
            }
        )
    }
)
```

### NestedNavigationGraph
**Purpose:** Renders an embedded Navigation 3 `NavDisplay` hosting a full back-stack of `MosaicScreen` destinations. Each `Entry` declares the screen it maps to with initial/failure tile+event trees.
**When to use:** Embedding a nested navigation flow inside a screen (e.g. a master-detail layout, or a section with its own back stack).
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.navigation.NestedNavigationGraph`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `navigatorId` | `String` | required | Key to register in `NavigatorsHolder` |
| `startEntryId` | `String` | required | screenId of the initial entry |
| `defaultTransition` | `ContentTransitionSchema?` | `null` | |
| `defaultPopTransition` | `ContentTransitionSchema?` | `null` | |
| `defaultPredictivePopTransition` | `ContentTransitionSchema?` | `null` | |
| `entries` | `NestedNavigationGraphEntryBuilderScope.() -> Unit` | required | Use `entry(screenId, initialTiles, initialEvents, failureTiles, failureEvents)` |

**Updatable via UpdateTiles:** `style`, `visibility`, `entries`, `startEntryId`, `defaultTransition`, `defaultPopTransition`, `defaultPredictivePopTransition`
**Triggers dispatched:** `OnNavigationEntrySet` (on every destination change), `OnNavigationEntryChanged`
**Notes:** Navigator is unregistered in `DisposableEffect` on composition exit. Per-entry transitions override graph-level defaults.

**Example:**
```kotlin
NestedNavigationGraph(
    id = "mainGraph",
    navigatorId = "main_navigator",
    startEntryId = "home",
    entries = {
        entry(
            screenId = "home",
            initialTiles = { HomeScreen() },
            initialEvents = {
                GetScreen(trigger = EventTriggers.onDisplay())
            }
        )
        entry(
            screenId = "detail",
            initialTiles = { DetailScreen() },
            failureTiles = { SimpleText(text = "Error loading detail") }
        )
    }
)
```

---

## Progress

### CircularProgressIndicator
**Purpose:** Renders a Material 3 `CircularProgressIndicator`. Determinate when `progress` is non-null (0.0–1.0), indeterminate (spinning) when `null`.
**When to use:** Loading states — spinning while waiting, or showing upload/download progress as a fraction.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.progress.CircularProgressIndicator`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `progress` | `Float?` | `null` | null = indeterminate; 0.0–1.0 = determinate |

**Updatable via UpdateTiles:** `style`, `visibility`, `progress`
**Triggers dispatched:** None.

**Example:**
```kotlin
CircularProgressIndicator(
    id = "uploadProgress",
    progress = null,
    style = { size(fixedHorizontally(48), fixedVertically(48)) }
)
```

### LinearProgressIndicator
**Purpose:** Renders a Material 3 `LinearProgressIndicator`. Determinate when `progress` is non-null, indeterminate (animated) when `null`.
**When to use:** Full-width progress bars for downloads, form submissions, or multi-step operations.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.progress.LinearProgressIndicator`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `progress` | `Float?` | `null` | null = indeterminate; 0.0–1.0 = determinate |

**Updatable via UpdateTiles:** `style`, `visibility`, `progress`
**Triggers dispatched:** None.

**Example:**
```kotlin
LinearProgressIndicator(
    id = "downloadBar",
    progress = 0.45f,
    style = { size(fillHorizontally(), wrapVertically()) }
)
```

---

## Search

### SearchBar
**Purpose:** Renders a custom search bar built on `BasicTextField` with Material 3 surface styling. Includes optional placeholder, leading icon tile, and a built-in animated clear button.
**When to use:** Screen-level search input that drives filtering or navigation.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.search.SearchBar`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `query` | `String` | `""` | Server-controlled current query |
| `placeholder` | `String?` | `null` | |
| `leadingIcon` | `(TileSchemaBuilderScope.() -> Unit)?` | `null` | Tile rendered as leading icon |
| `trailingIcon` | `(TileSchemaBuilderScope.() -> Unit)?` | `null` | Not rendered by current renderer |

**Updatable via UpdateTiles:** `style`, `visibility`, `query`, `placeholder`, `leadingIcon`
**Triggers dispatched:** `OnQueryChanged` (new query as incomingData), `OnQueryCleared` (clear button tapped), `OnSearch` (IME search action)
**Notes:** Fixed height 56dp. Because query is server-driven, the server must update `query` via UpdateTiles on each `onQueryChanged` to keep the display in sync.

**Example:**
```kotlin
SearchBar(
    id = "searchBar",
    placeholder = "Search environments...",
    leadingIcon = { Icon(id = "search_icon", icon = icon("search")) },
    events = {
        UpdateTiles(
            trigger = EventTriggers.onQueryChanged(),
            updates = {
                update(tileId = "searchBar", updateData = incomingTileUpdateData())
                update(tileId = "environmentList", updateData = incomingTileUpdateData())
            }
        )
        UpdateTiles(
            trigger = EventTriggers.onQueryCleared(),
            updates = {
                update(tileId = "searchBar", updateData = inlineTileUpdateData(mapOf("query" to "")))
                update(tileId = "environmentList", updateData = inlineTileUpdateData(mapOf("filterChildrenByTerm" to null)))
            }
        )
    }
)
```

---

## Badges

### Badge
**Purpose:** Renders a Material 3 `Badge` — a small colored dot or pill. When `content` is non-null, the text is displayed inside the badge pill; when null, a plain dot is rendered.
**When to use:** Notification counts, unread indicators, status markers on icons or navigation items.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.badges.Badge`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `content` | `String?` | `null` | null = dot only; non-null = pill with text |

**Updatable via UpdateTiles:** `style`, `visibility`, `content`
**Triggers dispatched:** None.
**Notes:** Typically used as a child inside a parent `Box`. Not interactive by itself.

**Example:**
```kotlin
Box(id = "notifBox") {
    IconButton(
        id = "notifBtn",
        icon = icon("notifications"),
        events = {
            Navigate(trigger = EventTriggers.onClick(), screenId = "notifications")
        }
    )
    Badge(id = "notifBadge", content = "3")
}
```

---

## App Bars

### TopAppBar
**Purpose:** Renders a Material 3 top app bar in one of four styles: `DEFAULT` (small, left-aligned), `CENTER_ALIGNED`, `MEDIUM` (collapsible), or `LARGE` (collapsible).
**When to use:** Screen-level header with title, optional back button, and action icons.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.app_bars.TopAppBar`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `title` | `TileSchemaBuilderScope.() -> Unit` | required | Title slot (any tile) |
| `navigationIcon` | `(TileSchemaBuilderScope.() -> Unit)?` | `null` | Leading icon (e.g. back button) |
| `actions` | `(TileSchemaBuilderScope.() -> Unit)?` | `null` | Trailing action tiles |
| `barStyle` | `TopAppBarTileSchema.TopAppBarStyle` | `DEFAULT` | DEFAULT, CENTER_ALIGNED, MEDIUM, LARGE |

**Updatable via UpdateTiles:** `style`, `visibility`, `title`, `navigationIcon`, `actions`, `barStyle`
**Triggers dispatched:** None directly — interaction triggers come from child tiles in `navigationIcon` or `actions`.
**Notes:** Title, navigation icon, and action tiles are rendered as independent composable lambdas, not in the normal `RenderChildren` hierarchy.

**Example:**
```kotlin
TopAppBar(
    id = "topBar",
    barStyle = TopAppBarTileSchema.TopAppBarStyle.DEFAULT,
    navigationIcon = {
        IconButton(
            id = "backBtn",
            icon = icon("arrow_back"),
            events = {
                NavigateUp(trigger = EventTriggers.onClick())
            }
        )
    },
    actions = {
        IconButton(
            id = "settingsBtn",
            icon = icon("settings"),
            events = {
                Navigate(trigger = EventTriggers.onClick(), screenId = "settings")
            }
        )
    }
) {
    SimpleText(text = "Environments", typography = titleLarge())
}
```

### BottomAppBar
**Purpose:** Renders a Material 3 `BottomAppBar` with a row of `actions` tiles on the left and an optional FAB anchored to the right.
**When to use:** Bottom action bars combining multiple actions with a primary FAB.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.app_bars.BottomAppBar`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `actions` | `TileSchemaBuilderScope.() -> Unit` | `{}` | Left-side action tiles (rendered in RowScope) |
| `floatingActionButton` | `(TileSchemaBuilderScope.() -> Unit)?` | `null` | FAB anchored to right |

**Updatable via UpdateTiles:** `style`, `visibility`, `actions`, `floatingActionButton`
**Triggers dispatched:** None directly — from child tiles.
**Notes:** `actions` tiles are rendered in `RowScope`; children can use `LocalRowScope` for `weight` modifiers.

**Example:**
```kotlin
BottomAppBar(
    id = "bottomBar",
    actions = {
        IconButton(
            id = "deleteBtn",
            icon = icon("delete"),
            events = {
                SendNetworkRequest(trigger = EventTriggers.onClick(), networkRequestData = deleteNetworkRequestData)
            }
        )
        IconButton(
            id = "shareBtn",
            icon = icon("share"),
            events = {
                TriggerEvent(eventId = "shareEvent", trigger = EventTriggers.onClick())
            }
        )
    },
    floatingActionButton = {
        FloatingActionButton(
            id = "addFab",
            icon = icon("add"),
            events = {
                Navigate(trigger = EventTriggers.onClick(), screenId = "create")
            }
        )
    }
)
```

---

## Inputs

### DropdownList
**Purpose:** Renders a Material 3 `ExposedDropdownMenuBox` with a list of selectable options, in filled or outlined style.
**When to use:** Form fields requiring the user to pick one item from a predefined list.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.inputs.DropdownList`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `options` | `List<SelectOption>` | required | Built with `selectOption(id, label)` helper |
| `selectedOptionId` | `String` | required | Must match an id in `options` — validated at build time |
| `enabled` | `Boolean` | `true` | |
| `kind` | `DropdownListTileSchema.Kind` | `OUTLINED` | `filledDropdownList()` / `outlinedDropdownList()` |

`SelectOption`: `{ id: String, label: String }` — build with `selectOption(id, label)`.

**Updatable via UpdateTiles:** `selectedOptionId`, `enabled`, `options`, `kind`, `visibility`, `style`

**Form tile:** `produceValueWithKey` always returns `mapOf(key to selectedOptionId)`. No nullable/placeholder concept — `selectedOptionId` is always valid. To represent a "please select" state, include a dedicated option (e.g., `selectOption("none", "Selecione...")`) and pass its id as the initial `selectedOptionId`.

**Server contract:** Server always sends `expanded = false`; the client owns the open/closed state.

**Triggers dispatched:** `OnDropdownListItemSelected(id)`, `OnDropdownListOpen`, `OnDropdownListClose`

**Example:**
```kotlin
DropdownList(
    id = "dd_status",
    options = listOf(
        selectOption("active", "Ativo"),
        selectOption("inactive", "Inativo"),
        selectOption("pending", "Pendente"),
    ),
    selectedOptionId = "active",
    kind = outlinedDropdownList(),
    events = {
        UpdateTiles(
            trigger = EventTriggers.onDropdownListItemSelected("inactive"),
            updates = {
                update(tileId = "warning_text", updateData = inlineTileUpdateData("visibility" to "VISIBLE"))
            }
        )
    }
)
```


---

## System

### SystemBroadcastListener
**Purpose:** Transparent container tile that renders its children directly and automatically listens for system broadcasts, firing `onSystemBroadcast` triggers when they arrive.
**When to use:** When you need to react to system-level broadcasts (`BroadcastToSystem`) sent from any screen or event in the app, without introducing any visual wrapper around an existing subtree.
**Import:** `import dev.catbit.mosaic.server.builder.tile.builders.system.SystemBroadcastListener`

**Fields:**
| Field | Type | Default | Notes |
|---|---|---|---|
| `tiles` | `TileSchemaBuilderScope.() -> Unit` | required (trailing lambda) | Child tiles rendered directly with no wrapping layout |

**Updatable via UpdateTiles:** `tiles`, `visibility`, `style`

**Triggers dispatched:** `OnSystemBroadcast(broadcastId)` — fired automatically by the renderer via `observeSystemBroadcastChannel` whenever a `BroadcastToSystem` event with a matching `broadcastId` is emitted anywhere in the app. The broadcast payload is forwarded as `incomingData` to child events.

**How it works:** The renderer subscribes to the app-wide broadcast channel on composition and calls `triggerEvent(onSystemBroadcastEventTrigger(data.broadcastId), data = data.data)` for each received broadcast. Events attached to this tile with `onSystemBroadcast("id")` as trigger will fire when the matching broadcast arrives.

**Example:**
```kotlin
SystemBroadcastListener(id = "session_listener") {
    events = {
        Navigate(
            trigger = EventTriggers.onSystemBroadcast("session_expired"),
            destination = "login",
            navigatorId = "main"
        )
        UpdateTiles(
            trigger = EventTriggers.onSystemBroadcast("refresh_list"),
            updates = {
                update(tileId = "lazy_col", updateData = inlineTileUpdateData("tiles" to emptyList<Any>()))
            }
        )
    }
    tiles {
        SimpleText(id = "label", text = "Content")
    }
}
