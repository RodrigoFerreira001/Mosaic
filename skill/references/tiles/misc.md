# Tiles: Miscellaneous (Badge, Chip, SearchBar, Menu)

---

## BadgeTileSchema

**@SerialName:** `"Badge"`

Material3 badge (small dot or number indicator).

### Fields
| Field | Type | Default |
|---|---|---|
| `content` | `String?` | null (null = dot badge, value = text badge) |

### Supported Triggers
None.

### Builder
```kotlin
Badge(content = "3")    // "3" badge
Badge()                  // dot badge
```

---

## SuggestionChipTileSchema

**@SerialName:** `"SuggestionChip"`

### Fields
| Field | Type | Default |
|---|---|---|
| `text` | `String` | required |
| `enabled` | `Boolean` | `true` |

### Supported Triggers
`OnClickEventTrigger`

### Builder
```kotlin
SuggestionChip(text = "Kotlin", enabled = true) {
    SendNetworkRequest(
        url = "/search?tag=kotlin",
        method = HttpMethod.GET,
        trigger = OnClickEventTrigger()
    ) { /* child events */ }
}
```

### Renderer
`SuggestionChipTileRenderer` → Material3 `SuggestionChip`.

---

## SearchBarTileSchema

**@SerialName:** `"SearchBar"`

### Fields
| Field | Type | Default |
|---|---|---|
| `query` | `String` | `""` |
| `placeholder` | `String?` | null |
| `leadingIcon` | `TileSchema?` | null |
| `trailingIcon` | `TileSchema?` | null |

### Supported Triggers
`OnTextChangedEventTrigger`

### Builder
```kotlin
SearchBar(
    query = "",
    placeholder = "Search...",
    leadingIcon = Icon(icon = IconSchema("search")),
    trailingIcon = IconButton(icon = IconSchema("close")) {
        UpdateTiles(
            groupingTileId = "root",
            trigger = OnClickEventTrigger(),
            updates = listOf(Update("searchbar-id", mapOf("query" to "")))
        )
    }
) {
    SendNetworkRequest(
        url = "/search",
        method = HttpMethod.GET,
        trigger = OnTextChangedEventTrigger()
    ) { /* update results */ }
}
```

---

## MenuTileSchema

**@SerialName:** `"Menu"`

Dropdown context menu. The first `tiles` item is the anchor; the `items` list defines menu entries.

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required (anchor tile, usually a button) |
| `items` | `List<MenuItem>` | required |
| `expanded` | `Boolean` | `false` |

`MenuItem`: `id: String`, `label: String`, `leadingIcon: IconSchema?`, `trailingIcon: IconSchema?`

### Supported Triggers
None directly. Use `ToggleMenuEventSchema` to open/close.
Item clicks fire through `OnMenuItemClickEventTrigger(itemId)`.

### Builder
```kotlin
Menu(
    id = "options-menu",
    expanded = false,
    tiles = {
        IconButton(icon = IconSchema("more_vert")) {
            ToggleMenu(menuId = "options-menu", trigger = OnClickEventTrigger())
        }
    }
) {
    item(id = "edit", label = "Edit", leadingIcon = IconSchema("edit"))
    item(id = "delete", label = "Delete", leadingIcon = IconSchema("delete"))

    on(OnMenuItemClickEventTrigger(itemId = "delete")) {
        // handle delete
    }
}
```

### Renderer
`MenuTileRenderer` → Material3 `DropdownMenu`.
