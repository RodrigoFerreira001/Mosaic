# Tiles: Buttons

---

## ButtonTileSchema

**@SerialName:** `"Button"`

### Fields

| Field | Type | Default |
|---|---|---|
| `id` | `String` | required |
| `events` | `List<EventSchema>?` | null |
| `style` | `StyleSchema` | required |
| `visibility` | `TileSchema.Visibility` | `VISIBLE` |
| `text` | `String` | required |
| `icon` | `IconSchema?` | null |
| `buttonType` | `ButtonTileSchema.Type` | `FILLED` |
| `shape` | `ButtonTileSchema.Shape` | `ROUNDED` |
| `loading` | `Boolean` | `false` |
| `enabled` | `Boolean` | `true` |

`Type`: `FILLED`, `ELEVATED`, `FILLED_TONAL`, `OUTLINED`, `TEXT`
`Shape`: `SQUARE`, `ROUNDED`

### Supported Triggers
`OnClickEventTrigger`, `OnLongPressEventTrigger`

### Builder

```kotlin
Button(
    text = "Submit",
    id = "btn-submit",
    icon = IconSchema(name = "send"),
    buttonType = ButtonTileSchema.Type.FILLED,
    shape = ButtonTileSchema.Shape.ROUNDED,
    loading = false,
    enabled = true,
    style = { fillHorizontally() }
) {
    Navigate(destination = "next", navigatorId = "main", trigger = OnClickEventTrigger())
}
```

Helper functions: `filledButton()`, `elevatedButton()`, `filledTonalButton()`, `outlinedButton()`, `textButton()`

### Renderer
`ButtonTileRenderer` → Material3 `Button`, `ElevatedButton`, `FilledTonalButton`, `OutlinedButton`, or `TextButton`.
Shows `CircularProgressIndicator` when `loading = true`.

---

## IconButtonTileSchema

**@SerialName:** `"IconButton"`

### Fields

| Field | Type | Default |
|---|---|---|
| `icon` | `IconSchema` | required |
| `buttonType` | `IconButtonTileSchema.Type` | `DEFAULT` |
| `enabled` | `Boolean` | `true` |

`Type`: `DEFAULT`, `FILLED`, `FILLED_TONAL`, `OUTLINED`

### Supported Triggers
`OnClickEventTrigger`

### Builder

```kotlin
IconButton(
    icon = IconSchema(name = "close"),
    buttonType = IconButtonTileSchema.Type.DEFAULT,
    enabled = true
) {
    DismissDialog(trigger = OnClickEventTrigger())
}
```

### Renderer
`IconButtonTileRenderer` → Material3 icon button variant.

---

## FloatingActionButtonTileSchema

**@SerialName:** `"FloatingActionButton"`

### Fields

| Field | Type | Default |
|---|---|---|
| `icon` | `IconSchema` | required |
| `size` | `FloatingActionButtonTileSchema.Size` | `DEFAULT` |
| `enabled` | `Boolean` | `true` |

`Size`: `DEFAULT`, `MEDIUM`, `LARGE`

### Supported Triggers
`OnClickEventTrigger`

### Builder

```kotlin
FloatingActionButton(
    icon = IconSchema(name = "add"),
    size = FloatingActionButtonTileSchema.Size.DEFAULT,
    enabled = true
) {
    Navigate(destination = "create", navigatorId = "main", trigger = OnClickEventTrigger())
}
```

### Renderer
`FloatingActionButtonTileRenderer` → Material3 `FloatingActionButton`.

---

## IconSchema

Used by multiple tiles:

```kotlin
IconSchema(
    name = "home",           // Material Symbol name
    color = ColorSchema.Theme(Color.Primary), // optional
    size = 24,               // optional, dp
    style = IconSchema.Style.ROUNDED  // OUTLINED, ROUNDED, SHARP
)
```
