# Tiles: Images & Icons

---

## ImageTileSchema

**@SerialName:** `"Image"`

Renders a local drawable resource.

### Fields
| Field | Type | Default |
|---|---|---|
| `resourceName` | `String` | required (drawable resource name) |
| `contentDescription` | `String?` | null |
| `contentScale` | `ContentScale` | required |
| `alpha` | `Float` | `1.0f` |
| `alignment` | `AlignmentSchema.TwoDimensional` | `Center` |

`ContentScale`: `CROP`, `FIT`, `FILL_HEIGHT`, `FILL_WIDTH`, `INSIDE`, `FILL_BOUNDS`

### Supported Triggers
None.

### Builder
```kotlin
Image(
    resourceName = "ic_logo",
    contentScale = ContentScale.FIT,
    contentDescription = "App logo",
    style = { fixedHorizontally(120); fixedVertically(80) }
)
```

The `resourceName` must match a drawable registered in `drawableResources` map passed to `MosaicApplication`.

---

## AsyncImageTileSchema

**@SerialName:** `"AsyncImage"`

Renders an image loaded from a URL (uses Coil 3).

### Fields
| Field | Type | Default |
|---|---|---|
| `url` | `String` | required |
| `contentDescription` | `String?` | null |
| `contentScale` | `ContentScale` | required |
| `alpha` | `Float` | `1.0f` |
| `clipToBounds` | `Boolean` | `false` |
| `alignment` | `AlignmentSchema.TwoDimensional` | `Center` |

### Supported Triggers
`OnAsyncImageLoadStartEventTrigger`, `OnAsyncImageLoadSuccessEventTrigger`, `OnAsyncImageLoadFailureEventTrigger`

### Builder
```kotlin
AsyncImage(
    url = "https://example.com/photo.jpg",
    contentScale = ContentScale.CROP,
    contentDescription = "Profile photo",
    style = { fixedHorizontally(80); fixedVertically(80); clip(circleShape()) }
) {
    // optional: handle load states
}
```

---

## IconTileSchema

**@SerialName:** `"Icon"`

Renders a Material Symbol icon.

### Fields
| Field | Type | Default |
|---|---|---|
| `icon` | `IconSchema` | required |

### Supported Triggers
None.

### Builder
```kotlin
Icon(
    icon = IconSchema(
        name = "favorite",
        color = ColorSchema.Theme(Color.Primary),
        size = 24,
        style = IconSchema.Style.ROUNDED
    )
)
```

---

## IconSchema

Reused across many tiles (Button, TopAppBar, NavigationBar, etc.):

```kotlin
IconSchema(
    name = "home",                         // Material Symbol name (snake_case)
    color = ColorSchema.Theme(Color.OnSurface), // optional
    size = 24,                             // optional, in dp
    style = IconSchema.Style.OUTLINED      // OUTLINED, ROUNDED, SHARP
)
```
