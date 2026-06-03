# Tiles: Inputs

---

## TextFieldTileSchema

**@SerialName:** `"TextField"` (base class, no direct @SerialName on abstract)

### Fields

| Field | Type | Default |
|---|---|---|
| `value` | `String` | `""` |
| `enabled` | `Boolean` | `true` |
| `leadingIcon` | `IconSchema?` | null |
| `clickableLeadingIcon` | `Boolean` | `false` |
| `trailingIcon` | `IconSchema?` | null |
| `clickableTrailingIcon` | `Boolean` | `true` |
| `prefixText` | `String?` | null |
| `suffixText` | `String?` | null |
| `placeholder` | `String?` | null |
| `label` | `String?` | null |
| `supportingText` | `String?` | null |
| `minLines` | `Int` | `1` |
| `maxLines` | `Int` | `Int.MAX_VALUE` |
| `kind` | `TextFieldTileSchema.Kind` | `OUTLINED` |
| `state` | `TextFieldTileSchema.State` | `NORMAL` |
| `keyboardOptions` | `KeyboardOptions?` | null |
| `visualTransformation` | `VisualTransformation?` | null |

`Kind`: `FILLED`, `OUTLINED`
`State`: `NORMAL`, `ERROR`

### KeyboardOptions

Use the `keyboardOptions(...)` helper from `dev.catbit.mosaic.server.builder.tile.builders.inputs`:

```kotlin
keyboardOptions(
    autoCorrectEnabled = null,      // Boolean? — null = unset
    howKeyboardOnFocus = null,      // Boolean? — null = unset
    capitalization = keyboardCapitalizationUnspecified(),
    keyboardType = keyboardTypeText(),
    imeAction = keyboardImeActionDone()
)
```

**`imeAction` helpers** (all in `dev.catbit.mosaic.server.builder.tile.builders.inputs`):
`keyboardImeActionUnspecified()`, `keyboardImeActionDefault()`, `keyboardImeActionNone()`,
`keyboardImeActionGo()`, `keyboardImeActionSearch()`, `keyboardImeActionSend()`,
`keyboardImeActionPrevious()`, `keyboardImeActionNext()`, `keyboardImeActionDone()`

**`keyboardType` helpers:**
`keyboardTypeUnspecified()`, `keyboardTypeText()`, `keyboardTypeAscii()`, `keyboardTypeNumber()`,
`keyboardTypePhone()`, `keyboardTypeUri()`, `keyboardTypeEmail()`, `keyboardTypePassword()`,
`keyboardTypeNumberPassword()`, `keyboardTypeDecimal()`

**`capitalization` helpers:**
`keyboardCapitalizationUnspecified()`, `keyboardCapitalizationNone()`, `keyboardCapitalizationCharacters()`,
`keyboardCapitalizationWords()`, `keyboardCapitalizationSentences()`

### VisualTransformation

Use helpers from `dev.catbit.mosaic.server.builder.tile.builders.inputs`:
```kotlin
keyboardVisualTransformationNone()
keyboardVisualTransformationPassword()
keyboardVisualTransformationCustom(mask = "*")
```

### Supported Triggers
`EventTriggers.onTextChanged()`, `EventTriggers.onKeyboardDone()`, `EventTriggers.onKeyboardGo()`,
`EventTriggers.onKeyboardNext()`, `EventTriggers.onKeyboardPrevious()`, `EventTriggers.onKeyboardSearch()`,
`EventTriggers.onKeyboardSend()`, `EventTriggers.onLeadingIconClick()`, `EventTriggers.onTrailingIconClick()`

### Builder
```kotlin
TextField(
    id = "email",
    placeholder = "E-mail",
    maxLines = 1,
    keyboardOptions = keyboardOptions(
        keyboardType = keyboardTypeEmail(),
        imeAction = keyboardImeActionNext()
    ),
    style = {
        size(width = fillHorizontally(max = 400))
        margin(horizontal = 24, bottom = 8)
    },
    events = {
        UpdateTiles(
            trigger = EventTriggers.onTextChanged(),
            updates = {
                update(tileId = "email", data = mapOf("state" to "NORMAL", "supportingText" to null))
            }
        )
        TriggerEvent(eventId = "submitEvent", trigger = EventTriggers.onKeyboardDone())
    }
)
```

### Renderer
`TextFieldTileRenderer` → Material3 `TextField` or `OutlinedTextField`.

---

## CheckboxTileSchema

**@SerialName:** `"Checkbox"`

### Fields
| Field | Type | Default |
|---|---|---|
| `checked` | `Boolean` | required |
| `enabled` | `Boolean` | `true` |

### Supported Triggers
`OnCheckEventTrigger`, `OnUncheckEventTrigger`, `OnCheckChangedEventTrigger`

### Builder
```kotlin
Checkbox(
    checked = false,
    enabled = true
) {
    // events
}
```

### Renderer
`CheckboxTileRenderer` → Material3 `Checkbox`.

---

## SwitchTileSchema

**@SerialName:** `"Switch"`

### Fields
| Field | Type | Default |
|---|---|---|
| `checked` | `Boolean` | required |
| `enabled` | `Boolean` | `true` |

### Supported Triggers
`OnCheckEventTrigger`, `OnUncheckEventTrigger`, `OnCheckChangedEventTrigger`

### Builder
```kotlin
Switch(checked = false, enabled = true) { /* events */ }
```

### Renderer
`SwitchTileRenderer` → Material3 `Switch`.

---

## RadioButtonTileSchema

**@SerialName:** `"RadioButton"`

### Fields
| Field | Type | Default |
|---|---|---|
| `selected` | `Boolean` | required |
| `enabled` | `Boolean` | `true` |
| `groupId` | `String` | required |

### Supported Triggers
`OnSelectEventTrigger`

### Builder
```kotlin
RadioButton(selected = false, enabled = true, groupId = "payment-method") { /* events */ }
```

### Renderer
`RadioButtonTileRenderer` → Material3 `RadioButton`. Group identified by `groupId`.
