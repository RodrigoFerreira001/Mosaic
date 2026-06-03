# Tiles: App Bars

---

## TopAppBarTileSchema

**@SerialName:** `"TopAppBar"`

### Fields
| Field | Type | Default |
|---|---|---|
| `title` | `TileSchema` | required (typically `SimpleTextTileSchema`) |
| `navigationIcon` | `TileSchema?` | null (typically `IconButtonTileSchema`) |
| `actions` | `List<TileSchema>?` | null (typically list of `IconButtonTileSchema`) |
| `barStyle` | `TopAppBarTileSchema.TopAppBarStyle` | `DEFAULT` |

`TopAppBarStyle`: `DEFAULT`, `CENTER_ALIGNED`, `MEDIUM`, `LARGE`

### Supported Triggers
None (triggers come from child tiles like IconButton).

### Builder
```kotlin
TopAppBar(
    title = SimpleText("Screen Title"),
    navigationIcon = IconButton(icon = IconSchema("arrow_back")) {
        NavigateUp(navigatorId = "main", trigger = OnClickEventTrigger())
    },
    actions = listOf(
        IconButton(icon = IconSchema("more_vert")) {
            ToggleMenu(menuId = "options-menu", trigger = OnClickEventTrigger())
        }
    ),
    barStyle = TopAppBarTileSchema.TopAppBarStyle.DEFAULT
)
```

### Renderer
`TopAppBarTileRenderer` → Material3 `TopAppBar`, `CenterAlignedTopAppBar`, `MediumTopAppBar`, or `LargeTopAppBar`.

---

## BottomAppBarTileSchema

**@SerialName:** `"BottomAppBar"`

### Fields
| Field | Type | Default |
|---|---|---|
| `actions` | `List<TileSchema>` | required (action tiles) |
| `floatingActionButton` | `TileSchema?` | null (typically `FloatingActionButtonTileSchema`) |

### Supported Triggers
None (triggers come from child tiles).

### Builder
```kotlin
BottomAppBar(
    actions = listOf(
        IconButton(icon = IconSchema("attach_file")) {},
        IconButton(icon = IconSchema("event")) {}
    ),
    floatingActionButton = FloatingActionButton(icon = IconSchema("add")) {
        Navigate(destination = "create", navigatorId = "main", trigger = OnClickEventTrigger())
    }
)
```

### Renderer
`BottomAppBarTileRenderer` → Material3 `BottomAppBar`.
