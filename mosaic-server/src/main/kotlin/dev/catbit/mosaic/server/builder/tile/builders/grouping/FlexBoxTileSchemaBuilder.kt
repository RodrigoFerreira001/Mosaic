package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema.FlexAlignContentSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema.FlexAlignItemsSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema.FlexDirectionSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema.FlexJustifyContentSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema.FlexWrapSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class FlexBoxTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val filterChildrenByTerm: String?,
    private val visibility: TileSchema.Visibility,
    private val direction: FlexDirectionSchema,
    private val justifyContent: FlexJustifyContentSchema,
    private val alignItems: FlexAlignItemsSchema,
    private val alignContent: FlexAlignContentSchema,
    private val wrap: FlexWrapSchema,
    private val columnGap: Int,
    private val rowGap: Int
) : TileSchemaBuilder<FlexBoxTileSchema>() {

    override fun build() = FlexBoxTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        filterChildrenByTerm = filterChildrenByTerm,
        visibility = visibility,
        direction = direction,
        justifyContent = justifyContent,
        alignItems = alignItems,
        alignContent = alignContent,
        wrap = wrap,
        columnGap = columnGap,
        rowGap = rowGap
    )
}

/**
 * Renders a Compose CSS-flexbox layout hosting [tiles]. [direction] sets the main axis (and its
 * reversal), [justifyContent] distributes children along the main axis, [alignItems] aligns
 * children on the cross axis of a single line, [alignContent] distributes the lines themselves
 * (only takes effect when [wrap] allows multiple lines), and [wrap] controls whether items wrap.
 * [columnGap] / [rowGap] are in dp. Publishes its flex scope to children so they can use flex
 * modifiers (grow, shrink, basis, align-self). When [filterChildrenByTerm] is set, only children
 * whose `searchableTerms` contain that term (case-insensitive substring match) are rendered —
 * children without `searchableTerms` are filtered out. Never scrollable — every child is
 * composed eagerly. Dispatches `onDisplay` once when composed, `onClick` only when declared.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, `onClick`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param filterChildrenByTerm Term used to filter [tiles] by their own `searchableTerms`. Defaults to none (no filtering).
 * @param direction Main axis direction. Defaults to row.
 * @param justifyContent Distribution of children along the main axis. Defaults to start.
 * @param alignItems Alignment of children on the cross axis. Defaults to start.
 * @param alignContent Distribution of wrapped lines; only takes effect when [wrap] allows wrapping. Defaults to start.
 * @param wrap Whether items wrap onto multiple lines. Defaults to no wrap.
 * @param columnGap Horizontal spacing between items, in dp. Defaults to 0.
 * @param rowGap Vertical spacing between items, in dp. Defaults to 0.
 * @param tiles Child tiles laid out with flexbox semantics.
 */
fun TileSchemaBuilderScope.FlexBox(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    filterChildrenByTerm: String? = null,
    direction: FlexDirectionSchema = flexDirectionRow(),
    justifyContent: FlexJustifyContentSchema = flexJustifyStart(),
    alignItems: FlexAlignItemsSchema = flexAlignItemsStart(),
    alignContent: FlexAlignContentSchema = flexAlignContentStart(),
    wrap: FlexWrapSchema = flexNoWrap(),
    columnGap: Int = 0,
    rowGap: Int = 0,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        FlexBoxTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            filterChildrenByTerm = filterChildrenByTerm,
            visibility = visibility,
            direction = direction,
            justifyContent = justifyContent,
            alignItems = alignItems,
            alignContent = alignContent,
            wrap = wrap,
            columnGap = columnGap,
            rowGap = rowGap
        )
    )
}

/** Main axis runs left-to-right (or top-to-bottom is not applicable — this is the row direction). */
fun flexDirectionRow() = FlexDirectionSchema.Row

/** Main axis runs right-to-left, reversing item order. */
fun flexDirectionRowReverse() = FlexDirectionSchema.RowReverse

/** Main axis runs top-to-bottom. */
fun flexDirectionColumn() = FlexDirectionSchema.Column

/** Main axis runs bottom-to-top, reversing item order. */
fun flexDirectionColumnReverse() = FlexDirectionSchema.ColumnReverse


/** Packs children at the start of the main axis. */
fun flexJustifyStart() = FlexJustifyContentSchema.Start

/** Packs children at the center of the main axis. */
fun flexJustifyCenter() = FlexJustifyContentSchema.Center

/** Packs children at the end of the main axis. */
fun flexJustifyEnd() = FlexJustifyContentSchema.End

/** Distributes children with equal space between them, none at the edges. */
fun flexJustifySpaceBetween() = FlexJustifyContentSchema.SpaceBetween

/** Distributes children with equal space around each of them. */
fun flexJustifySpaceAround() = FlexJustifyContentSchema.SpaceAround

/** Distributes children with equal space between them and at both edges. */
fun flexJustifySpaceEvenly() = FlexJustifyContentSchema.SpaceEvenly


/** Aligns children to the start of the cross axis. */
fun flexAlignItemsStart() = FlexAlignItemsSchema.Start

/** Aligns children to the center of the cross axis. */
fun flexAlignItemsCenter() = FlexAlignItemsSchema.Center

/** Aligns children to the end of the cross axis. */
fun flexAlignItemsEnd() = FlexAlignItemsSchema.End

/** Stretches children to fill the cross axis. */
fun flexAlignItemsStretch() = FlexAlignItemsSchema.Stretch

/** Aligns children by their text baseline. */
fun flexAlignItemsBaseline() = FlexAlignItemsSchema.Baseline


/** Packs wrapped lines at the start of the cross axis. Only takes effect when wrapping is enabled. */
fun flexAlignContentStart() = FlexAlignContentSchema.Start

/** Packs wrapped lines at the center of the cross axis. Only takes effect when wrapping is enabled. */
fun flexAlignContentCenter() = FlexAlignContentSchema.Center

/** Packs wrapped lines at the end of the cross axis. Only takes effect when wrapping is enabled. */
fun flexAlignContentEnd() = FlexAlignContentSchema.End

/** Stretches wrapped lines to fill the cross axis. Only takes effect when wrapping is enabled. */
fun flexAlignContentStretch() = FlexAlignContentSchema.Stretch

/** Distributes wrapped lines with equal space between them. Only takes effect when wrapping is enabled. */
fun flexAlignContentSpaceBetween() = FlexAlignContentSchema.SpaceBetween

/** Distributes wrapped lines with equal space around each of them. Only takes effect when wrapping is enabled. */
fun flexAlignContentSpaceAround() = FlexAlignContentSchema.SpaceAround


/** Children are forced onto a single line, overflowing if needed. */
fun flexNoWrap() = FlexWrapSchema.NoWrap

/** Children wrap onto multiple lines when they overflow the main axis. */
fun flexWrap() = FlexWrapSchema.Wrap

/** Children wrap onto multiple lines in reverse order when they overflow the main axis. */
fun flexWrapReverse() = FlexWrapSchema.WrapReverse
