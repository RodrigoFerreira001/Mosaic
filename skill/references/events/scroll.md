# Events: Scroll Control

**All triggers use `EventTriggers.xxx()`.**

---

## ScrollColumnTileEventSchema

**@SerialName:** `"ScrollColumn"`

Programmatically scrolls a Column or LazyColumn.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.ScrollColumn(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tileId: String,
    where: ScrollColumnTileEventSchema.Where,
    smoothly: Boolean = true
)
```

`Where` helpers: `scrollToTop()`, `scrollTo(index = 5)`, `scrollToBottom()`

### Child Triggers
`EventTriggers.onScrolled()`

### Examples
```kotlin
// Scroll to top on button click:
IconButton(icon = icon("arrow_upward"), events = {
    ScrollColumn(
        tileId = "feed-list",
        where = scrollToTop(),
        smoothly = true,
        trigger = EventTriggers.onClick()
    )
})

// Scroll to bottom after adding new items:
AddTiles(
    trigger = EventTriggers.onSuccess(),
    groupingTileId = "messages-list",
    events = {
        ScrollColumn(
            tileId = "messages-list",
            where = scrollToBottom(),
            smoothly = true,
            trigger = EventTriggers.onTilesAdded()
        )
    }
) {
    // new message tile
}
```

---

## ScrollRowTileEventSchema

**@SerialName:** `"ScrollRow"`

Programmatically scrolls a Row or LazyRow.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.ScrollRow(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tileId: String,
    where: ScrollRowTileEventSchema.Where,
    smoothly: Boolean = true
)
```

`Where` helpers: `scrollToStart()`, `scrollTo(index)`, `scrollToEnd()`

### Example
```kotlin
// Scroll carousel to end after tiles are added:
AddTiles(
    trigger = EventTriggers.onSuccess(),
    groupingTileId = "carousel",
    events = {
        ScrollRow(
            tileId = "carousel",
            where = scrollToEnd(),
            smoothly = true,
            trigger = EventTriggers.onTilesAdded()
        )
    }
) { /* new item */ }
```

---

## ScrollPagerTileEventSchema

**@SerialName:** `"ScrollPager"`

Programmatically navigates a Pager.

### Builder Signature
```kotlin
fun EventSchemaBuilderScope.ScrollPager(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tileId: String,
    where: ScrollPagerTileEventSchema.Where,
    smoothly: Boolean = true
)
```

`Where`:
- `ScrollPagerTileEventSchema.Where.Begin` — first page
- `ScrollPagerTileEventSchema.Where.PreviousPage`
- `ScrollPagerTileEventSchema.Where.NextPage`
- `ScrollPagerTileEventSchema.Where.End` — last page

### Example
```kotlin
// Onboarding next/back buttons:
Row {
    Button("Back", buttonType = outlinedButton(), events = {
        ScrollPager(
            tileId = "onboarding-pager",
            where = ScrollPagerTileEventSchema.Where.PreviousPage,
            smoothly = true,
            trigger = EventTriggers.onClick()
        )
    })
    Button("Next", buttonType = filledButton(), events = {
        ScrollPager(
            tileId = "onboarding-pager",
            where = ScrollPagerTileEventSchema.Where.NextPage,
            smoothly = true,
            trigger = EventTriggers.onClick()
        )
    })
}
```
