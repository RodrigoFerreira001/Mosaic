# Tile: SimpleText

**Schema class:** `SimpleTextTileSchema`
**@SerialName:** `"Text"`
**Package:** `mosaic-core/.../schemas/tile/tiles/text/`

## Fields

| Field | Type | Default | Notes |
|---|---|---|---|
| `id` | `String` | required | Inherited |
| `events` | `List<EventSchema>?` | null | Inherited |
| `style` | `StyleSchema` | required | Inherited |
| `visibility` | `TileSchema.Visibility` | `VISIBLE` | Inherited |
| `text` | `String` | required | Text to display |
| `color` | `ColorSchema?` | null | See ColorSchema below |
| `typography` | `TypographySchema?` | null | See TypographySchema below |

## Supported Triggers
None.

## Builder

Extension function: `TileSchemaBuilderScope.SimpleText(...)`

```kotlin
SimpleText(
    text = "Hello World",
    id = "my-text",                            // optional, default = randomId()
    color = ColorSchema.Theme(Color.Primary),  // optional
    typography = TypographySchema.HEADLINE_MEDIUM, // optional
    style = { fillHorizontally() },
    visibility = TileSchema.Visibility.VISIBLE
)
```

## Renderer
`SimpleTextTileRenderer` → renders Material3 `Text()` composable.

---

## Supporting Types

### ColorSchema (sealed interface)
```kotlin
ColorSchema.Hex("#FF0000")                      // hex ARGB
ColorSchema.Rgba(r = 1f, g = 0f, b = 0f, alpha = 1f)
ColorSchema.Theme(Color.Primary)                // Material theme color
```

`Color` enum (27 values): `Primary`, `OnPrimary`, `PrimaryContainer`, `OnPrimaryContainer`,
`Secondary`, `OnSecondary`, `SecondaryContainer`, `OnSecondaryContainer`,
`Tertiary`, `OnTertiary`, `TertiaryContainer`, `OnTertiaryContainer`,
`Error`, `OnError`, `ErrorContainer`, `OnErrorContainer`,
`Background`, `OnBackground`, `Surface`, `OnSurface`, `SurfaceVariant`, `OnSurfaceVariant`,
`Outline`, `OutlineVariant`, `Scrim`, `InverseSurface`, `InverseOnSurface`

### TypographySchema (enum — 15 Material type styles)
`DISPLAY_LARGE`, `DISPLAY_MEDIUM`, `DISPLAY_SMALL`,
`HEADLINE_LARGE`, `HEADLINE_MEDIUM`, `HEADLINE_SMALL`,
`TITLE_LARGE`, `TITLE_MEDIUM`, `TITLE_SMALL`,
`BODY_LARGE`, `BODY_MEDIUM`, `BODY_SMALL`,
`LABEL_LARGE`, `LABEL_MEDIUM`, `LABEL_SMALL`
