# Tiles: Containers & Dynamic

---

## PagerTileSchema

**@SerialName:** `"Pager"`

Horizontal swipe-through pages.

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required (pages) |
| `pageSize` | `PageSizeSchema` | `Fill` |
| `pageSpacing` | `Int` | `0` |
| `contentPadding` | `Int` | `0` |
| `beyondViewportPageCount` | `Int` | `0` |

`PageSizeSchema`: `Fill` (full viewport) or `Fixed(value: Int)`

### Supported Triggers
`OnPageChangedEventTrigger`

### Builder
```kotlin
Pager(pageSize = PageSizeSchema.Fill, pageSpacing = 8) {
    // Each direct child = one page
    Column { /* page 1 */ }
    Column { /* page 2 */ }
}
```

---

## CarouselTileSchema

**@SerialName:** `"Carousel"`

Material3 carousel.

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `type` | `CarouselTypeSchema` | required |
| `itemSpacing` | `Int` | `0` |
| `contentPadding` | `Int` | `0` |
| `userScrollEnabled` | `Boolean` | `true` |

`CarouselTypeSchema`:
- `MultiBrowse(preferredItemWidth: Int, minSmallItemWidth: Int?, maxSmallItemWidth: Int?)`
- `Uncontained(itemWidth: Int)`

### Supported Triggers
None.

---

## CardTileSchema

**@SerialName:** `"Card"`

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required (card content) |
| `kind` | `CardTileSchema.Kind` | `DEFAULT` |

`Kind`: `DEFAULT`, `ELEVATED`, `OUTLINED`

### Supported Triggers
None.

### Builder
```kotlin
Card(kind = CardTileSchema.Kind.ELEVATED) {
    Column {
        SimpleText("Card title")
    }
}
```

---

## ShimmerTileSchema

**@SerialName:** `"Shimmer"`

Animated loading placeholder overlay.

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required (content to shimmer over) |

### Supported Triggers
None.

### Usage
Wrap skeleton/placeholder tiles with Shimmer while content loads. Replace with actual tiles via `ReplaceTiles` or `AddTiles` on success.

---

## PullToRefreshTileSchema

**@SerialName:** `"PullToRefresh"`

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `isRefreshing` | `Boolean` | `false` |

### Supported Triggers
`OnPullEventTrigger`

### Builder
```kotlin
PullToRefresh(isRefreshing = false) {
    LazyColumn { /* content */ }
    on(OnPullEventTrigger()) {
        UpdateTiles(groupingTileId = "root", trigger = InlineEventTrigger(),
            updates = listOf(Update("pull-refresh-id", mapOf("isRefreshing" to true))))
        // then SendNetworkRequest -> on success -> StopRefreshing
    }
}
```

Pair with `StopRefreshingEventSchema` to reset `isRefreshing` after data loads.

---

## LazyTilesTileSchema

**@SerialName:** `"LazyTiles"`

Tile that fetches its own content from an HTTP endpoint.

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>?` | null (loaded content) |
| `placeholderTiles` | `List<TileSchema>` | required |
| `failureTiles` | `List<TileSchema>` | required |
| `isFailureState` | `Boolean` | `false` |
| `url` | `String` | required |
| `method` | `HttpMethod` | required |
| `body` | `AnySerializable?` | null |
| `headers` | `Map<String, String>?` | null |

### Supported Triggers
None (driven by internal load cycle via `OnLoadTilesStart/Success/Failure` triggers).

### Builder
```kotlin
LazyTiles(
    url = "/api/items",
    method = HttpMethod.GET,
    placeholderTiles = { Shimmer { /* skeleton */ } },
    failureTiles = { SimpleText("Failed to load") }
)
```

Use `ReloadLazyTilesEventSchema` to trigger a reload programmatically.
