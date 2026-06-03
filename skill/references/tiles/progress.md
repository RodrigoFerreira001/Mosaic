# Tiles: Progress Indicators

---

## LinearProgressIndicatorTileSchema

**@SerialName:** `"LinearProgressIndicator"`

### Fields
| Field | Type | Default |
|---|---|---|
| `progress` | `Float?` | null (indeterminate) |

`progress`: `0.0` to `1.0` for determinate; `null` for indeterminate (animated loop).

### Supported Triggers
None.

### Builder
```kotlin
// Indeterminate (loading spinner feel):
LinearProgressIndicator()

// Determinate (e.g., 75% complete):
LinearProgressIndicator(progress = 0.75f)
```

### Renderer
`LinearProgressIndicatorTileRenderer` → Material3 `LinearProgressIndicator`.

---

## CircularProgressIndicatorTileSchema

**@SerialName:** `"CircularProgressIndicator"`

### Fields
| Field | Type | Default |
|---|---|---|
| `progress` | `Float?` | null (indeterminate) |

`progress`: `0.0` to `1.0` for determinate; `null` for indeterminate.

### Supported Triggers
None.

### Builder
```kotlin
// Indeterminate:
CircularProgressIndicator()

// Determinate:
CircularProgressIndicator(progress = 0.5f)
```

### Renderer
`CircularProgressIndicatorTileRenderer` → Material3 `CircularProgressIndicator`.

---

## Common Pattern: Loading State

Show an indeterminate indicator while loading, then swap tiles:

```kotlin
Column(id = "content") {
    CircularProgressIndicator()
}
// Screen-level event:
GetScreen(trigger = OnDisplayEventTrigger()) {
    ReplaceTiles(
        groupingTileId = "content",
        trigger = OnSuccessEventTrigger()
    ) {
        // actual content tiles
    }
}
```
