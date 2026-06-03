# Events: Tile Management

All tile management events accept `trigger`, `events`, and their own specific parameters.
**All triggers use `EventTriggers.xxx()`.**

---

## AddTilesEventSchema

**@SerialName:** `"AddTiles"`

Inserts one or more tiles into a container tile at a specific position.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.AddTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    groupingTileId: String,
    position: InsertionPosition = insertAtEnd(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    tiles: TileSchemaBuilderScope.() -> Unit
)
```

### InsertionPosition helpers
```kotlin
insertAtStart()
insertAtEnd()
insertBeforeTile(tileId = "anchor-id")
insertAfterTile(tileId = "anchor-id")
insertAtIndex(index = 3)
```

### Child Triggers
`EventTriggers.onSuccess()` — tiles inserted successfully
`EventTriggers.onFailure()` — grouping tile not found (`TileNotFoundException`); incomingData is the exception

### Example
```kotlin
SendNetworkRequest(
    trigger = EventTriggers.onClick(),
    url = "/api/items",
    method = HttpMethod.GET,
    events = {
        AddTiles(
            trigger = EventTriggers.onSuccess(),
            groupingTileId = "items-list",
            position = insertAtEnd(),
            events = {
                ScrollColumn(
                    trigger = EventTriggers.onSuccess(),
                    tileId = "items-list",
                    where = scrollToBottom(),
                    smoothly = true
                )
            }
        ) {
            // tiles to add (built from incomingData in a real Runner)
            Column(id = "item-1") { SimpleText("Item 1") }
        }
    }
)
```

---

## RemoveTilesEventSchema

**@SerialName:** `"RemoveTiles"`

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.RemoveTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    groupingTileId: String,
    tileIds: List<String>,
    events: EventSchemaBuilderScope.() -> Unit = {}
)
```

### Child Triggers
`EventTriggers.onSuccess()` — tiles removed successfully
`EventTriggers.onFailure()` — grouping tile not found (`TileNotFoundException`)

### Example
```kotlin
RemoveTiles(
    trigger = EventTriggers.onSuccess(),
    groupingTileId = "items-list",
    tileIds = listOf("item-42")
)
```

---

## ReplaceTilesEventSchema

**@SerialName:** `"ReplaceTiles"`

Replaces ALL children of a container with new tiles.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.ReplaceTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    groupingTileId: String,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tiles: TileSchemaBuilderScope.() -> Unit
)
```

### Child Triggers
`EventTriggers.onSuccess()` — tiles replaced successfully
`EventTriggers.onFailure()` — grouping tile not found (`TileNotFoundException`)

### Example
```kotlin
GetData(trigger = EventTriggers.onDisplay(), readings = { ... }, events = {
    ReplaceTiles(
        trigger = EventTriggers.onSuccess(),
        groupingTileId = "content-area"
    ) {
        SimpleText("Loaded!")
        Button("Refresh", events = {
            RefreshScreen(trigger = EventTriggers.onClick())
        })
    }
    ChangeScreenState(
        trigger = EventTriggers.onFailure(),
        state = ChangeScreenStateEventSchema.State.Failure
    )
})
```

---

## UpdateTilesEventSchema

**@SerialName:** `"UpdateTiles"`

Patches specific fields of existing tiles. Uses a scope-based `updates` lambda.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.UpdateTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    updates: UpdateTilesUpdateBuilderScope.() -> Unit
)

// Inside the updates lambda:
class UpdateTilesUpdateBuilderScope {
    fun update(tileId: String, data: Map<String, AnySerializable?>)
}
```

### Child Triggers
`EventTriggers.onTilesUpdated()` — fires after ALL tiles in the batch are updated
`EventTriggers.onSuccess()` — same timing as `onTilesUpdated`
`EventTriggers.onFailure()` — at least one tile ID was not found (`TileNotFoundException`)

### Example
```kotlin
// Loading state:
UpdateTiles(
    trigger = EventTriggers.onStart(),
    updates = {
        update("submit-btn", mapOf("loading" to true, "enabled" to false))
        update("cancel-btn", mapOf("enabled" to false))
    }
)

// Restore after success:
UpdateTiles(
    trigger = EventTriggers.onFailure(),
    updates = {
        update("submit-btn", mapOf("loading" to false, "enabled" to true))
        update("cancel-btn", mapOf("enabled" to true))
    }
)

// Update text content:
UpdateTiles(
    trigger = EventTriggers.onSuccess(),
    updates = {
        update("status-text", mapOf("text" to "Saved!"))
        update("email-field", mapOf("state" to "NORMAL", "supportingText" to null))
    }
)

// Validation error:
UpdateTiles(
    trigger = EventTriggers.onFailure(),
    updates = {
        update("email-field", mapOf(
            "state" to "ERROR",
            "supportingText" to "Invalid email",
            "trailingIcon" to icon("error")
        ))
    }
)
```

**The `data` map keys are the exact field names from the Schema** (e.g., `"text"`, `"loading"`, `"enabled"`, `"checked"`, `"state"`, `"supportingText"`, `"trailingIcon"`, `"visibility"`).

---

## WipeTilesEventSchema

**@SerialName:** `"WipeTiles"`

Removes ALL children from a container.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.WipeTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    groupingTileId: String,
    events: EventSchemaBuilderScope.() -> Unit = {}
)
```

### Child Triggers
`EventTriggers.onSuccess()` — all tiles wiped successfully
`EventTriggers.onFailure()` — grouping tile not found (`TileNotFoundException`)

### Example
```kotlin
WipeTiles(
    trigger = EventTriggers.onClick(),
    groupingTileId = "search-results",
    events = {
        UpdateTiles(trigger = EventTriggers.onSuccess(), updates = {
            update("search-bar", mapOf("query" to ""))
        })
    }
)
```

---

## ReloadLazyTilesEventSchema

**@SerialName:** `"ReloadLazyTiles"`

Triggers a reload of a `LazyTiles` tile (re-fetches its URL).

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.ReloadLazyTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    lazyTileId: String,
    events: EventSchemaBuilderScope.() -> Unit = {}
)
```

### Child Triggers
`EventTriggers.onSuccess()` — reload broadcast sent successfully
`EventTriggers.onFailure()` — lazy tile not found (`TileNotFoundException`)

### Example
```kotlin
Button("Refresh", events = {
    ReloadLazyTiles(trigger = EventTriggers.onClick(), lazyTileId = "product-catalog")
})
```
