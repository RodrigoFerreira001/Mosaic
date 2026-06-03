# Tiles: Layout & Grouping

All layout tiles hold child tiles via a `tiles: List<TileSchema>` field and accept a `TileSchemaBuilderScope.() -> Unit` lambda in the builder.

---

## Supporting Types

### AlignmentSchema (sealed)
- `Vertical`: `Top`, `Center`, `Bottom`
- `Horizontal`: `Start`, `Center`, `End`
- `TwoDimensional`: `TopStart`, `TopCenter`, `TopEnd`, `CenterStart`, `Center`, `CenterEnd`, `BottomStart`, `BottomCenter`, `BottomEnd`

### ArrangementSchema (sealed)
- `Vertical`: `Top`, `Bottom`, `Center`, `SpaceEvenly`, `SpaceBetween`, `SpaceAround`, `SpacedBy(space: Int, alignment?)`
- `Horizontal`: `Start`, `End`, `Center`, `SpaceEvenly`, `SpaceBetween`, `SpaceAround`, `SpacedBy(space: Int, alignment?)`

Builder helpers: `arrangeVerticallyToTop()`, `alignHorizontallyToStart()`, etc.

---

## BoxTileSchema

**@SerialName:** `"Box"`

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `alignment` | `AlignmentSchema.TwoDimensional` | `Center` |

### Supported Triggers
`OnClickEventTrigger`, `OnLongPressEventTrigger`

### Builder
```kotlin
Box(alignment = AlignmentSchema.TwoDimensional.Center) {
    SimpleText("centered")
}
```

---

## ColumnTileSchema

**@SerialName:** `"Column"`

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `arrangement` | `ArrangementSchema.Vertical` | `Top` |
| `alignment` | `AlignmentSchema.Horizontal` | `Start` |
| `scrollable` | `Boolean` | `false` |

### Supported Triggers
`OnClickEventTrigger`, `OnLongPressEventTrigger`, `OnScrolledEventTrigger`

### Builder
```kotlin
Column(scrollable = true, arrangement = arrangeVerticallyToTop()) {
    Button("A") {}
    Button("B") {}
}
```

---

## RowTileSchema

**@SerialName:** `"Row"`

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `arrangement` | `ArrangementSchema.Horizontal` | `Start` |
| `alignment` | `AlignmentSchema.Vertical` | `Top` |
| `scrollable` | `Boolean` | `false` |

### Supported Triggers
`OnClickEventTrigger`, `OnLongPressEventTrigger`, `OnScrolledEventTrigger`

### Builder
```kotlin
Row(scrollable = false) {
    Icon(icon = IconSchema("home"))
    SimpleText("Home")
}
```

---

## LazyColumnTileSchema

**@SerialName:** `"LazyColumn"`

Efficient vertical list — only renders visible items.

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `arrangement` | `ArrangementSchema.Vertical` | `Top` |
| `alignment` | `AlignmentSchema.Horizontal` | `Start` |
| `scrollThreshold` | `Int?` | null — items from end to trigger event |
| `considerLoadingItemAtEndOnThresholdReached` | `Boolean` | `true` |

### Supported Triggers
`OnClickEventTrigger`, `OnLongPressEventTrigger`, `OnScrolledEventTrigger`, `OnScrollThresholdReachedEventTrigger`

### Builder
```kotlin
LazyColumn(scrollThreshold = 3) {
    // items added dynamically via AddTiles event
}
```

Use `scrollThreshold` + `OnScrollThresholdReachedEventTrigger` for infinite scroll / pagination.

---

## LazyRowTileSchema

**@SerialName:** `"LazyRow"`

Same as LazyColumn but horizontal.

### Fields
Same as LazyColumn but with `ArrangementSchema.Horizontal` and `AlignmentSchema.Vertical`.

### Supported Triggers
Same as LazyColumn.

---

## FlexBoxTileSchema

**@SerialName:** `"FlexBox"`

CSS Flexbox-like layout (useful for web/desktop).

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `direction` | `FlexDirectionSchema` | `Row` |
| `justifyContent` | `FlexJustifyContentSchema` | `Start` |
| `alignItems` | `FlexAlignItemsSchema` | `Start` |
| `alignContent` | `FlexAlignContentSchema` | `Start` |
| `wrap` | `FlexWrapSchema` | `NoWrap` |
| `columnGap` | `Int` | `0` |
| `rowGap` | `Int` | `0` |

`FlexDirectionSchema`: `Row`, `RowReverse`, `Column`, `ColumnReverse`
`FlexJustifyContentSchema`: `Start`, `Center`, `End`, `SpaceBetween`, `SpaceAround`, `SpaceEvenly`
`FlexAlignItemsSchema`: `Start`, `End`, `Center`, `Stretch`, `Baseline`
`FlexWrapSchema`: `NoWrap`, `Wrap`, `WrapReverse`

### Supported Triggers
None.

---

## GridTileSchema

**@SerialName:** `"Grid"`

CSS Grid-like layout.

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `columns` | `List<GridTrackSchema>` | required |
| `rows` | `List<GridTrackSchema>` | `[]` |
| `columnGap` | `Int` | `0` |
| `rowGap` | `Int` | `0` |
| `flow` | `GridFlowSchema` | `Row` |

`GridTrackSchema`: `Fixed(value: Int)`, `Fraction(value: Float)`, `Flexible(value: Float)`, `Auto`, `MaxContent`, `MinContent`
`GridFlowSchema`: `Row`, `Column`

### Builder
```kotlin
Grid(columns = listOf(GridTrackSchema.Fraction(0.5f), GridTrackSchema.Fraction(0.5f))) {
    // 2-column grid items
}
```

---

## FlowRowTileSchema

**@SerialName:** `"FlowRow"`

Wrapping row — items wrap to new row when space runs out.

### Fields
| Field | Type | Default |
|---|---|---|
| `tiles` | `List<TileSchema>` | required |
| `horizontalArrangement` | `ArrangementSchema.Horizontal` | `Start` |
| `verticalArrangement` | `ArrangementSchema.Vertical` | `Top` |
| `maxItemsInEachRow` | `Int` | `Int.MAX_VALUE` |

### Supported Triggers
None.
