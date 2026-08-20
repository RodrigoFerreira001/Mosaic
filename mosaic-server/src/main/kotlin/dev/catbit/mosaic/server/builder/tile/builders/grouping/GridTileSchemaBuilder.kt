package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.GridTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.GridTileSchema.GridFlowSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.GridTileSchema.GridTrackSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible
import kotlinx.collections.immutable.toImmutableList

internal class GridTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val filterChildrenByTerm: String?,
    private val visibility: TileSchema.Visibility,
    private val flow: GridFlowSchema,
    private val columns: List<GridTrackSchema>,
    private val rows: List<GridTrackSchema>,
    private val columnGap: Int,
    private val rowGap: Int,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
) : TileSchemaBuilder<GridTileSchema>() {

    override fun build(): GridTileSchema {
        return GridTileSchema(
            id = id,
            tiles = TileSchemaBuilderScope().apply(tiles).build(),
            events = EventSchemaBuilderScope().apply(events).build(),
            style = StyleSchemaBuilderScope().apply(style).buildStyle(),
            searchableTerms = searchableTerms?.toImmutableList(),
            filterChildrenByTerm = filterChildrenByTerm,
            visibility = visibility,
            columns = columns.toImmutableList(),
            rows = rows.toImmutableList(),
            columnGap = columnGap,
            rowGap = rowGap,
            flow = flow
        )
    }
}

/**
 * Renders a Compose CSS-grid-like layout hosting [tiles]. [columns] and [rows] declare the
 * track template (each track fixed in dp, a fraction of available space, an `fr`-flexible unit,
 * auto, max-content or min-content); when [rows] is left empty, row tracks are derived
 * implicitly. [flow] decides whether children are placed row-first or column-first, and
 * [columnGap] / [rowGap] (in dp) set the spacing between tracks. Publishes its grid scope to
 * children so they can use grid modifiers (row/column span and placement). When
 * [filterChildrenByTerm] is set, only children whose `searchableTerms` contain that term
 * (case-insensitive substring match) are rendered — children without `searchableTerms` are
 * filtered out. Never scrollable — every child is composed eagerly. Dispatches `onDisplay` once
 * when composed, `onClick`/`onLongPress` only when declared on this tile.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, `onClick`, `onLongPress`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param filterChildrenByTerm Term used to filter [tiles] by their own `searchableTerms`. Defaults to none (no filtering).
 * @param flow Whether children are auto-placed row-first or column-first. Defaults to row-first.
 * @param columns Column track template.
 * @param rows Row track template. When empty, rows are derived implicitly.
 * @param columnGap Spacing between columns, in dp. Defaults to 0.
 * @param rowGap Spacing between rows, in dp. Defaults to 0.
 * @param tiles Child tiles placed into the grid.
 */
fun TileSchemaBuilderScope.Grid(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    filterChildrenByTerm: String? = null,
    flow: GridFlowSchema = flowGridThroughRows(),
    columns: List<GridTrackSchema>,
    rows: List<GridTrackSchema>,
    columnGap: Int = 0,
    rowGap: Int = 0,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        GridTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            filterChildrenByTerm = filterChildrenByTerm,
            visibility = visibility,
            flow = flow,
            columns = columns,
            rows = rows,
            columnGap = columnGap,
            rowGap = rowGap
        )
    )
}

/** Auto-places children row by row, filling each row before moving to the next. */
fun flowGridThroughRows() = GridFlowSchema.Row

/** Auto-places children column by column, filling each column before moving to the next. */
fun flowGridThroughColumns() = GridFlowSchema.Column


/** Row track with a fixed size in dp. */
fun gridRowFixed(value: Int): GridTrackSchema = GridTrackSchema.Fixed(value)

/** Row track sized as a fraction of the available space (0f–1f). */
fun gridRowFraction(value: Float): GridTrackSchema = GridTrackSchema.Fraction(value)

/** Row track sized with an `fr`-flexible unit, sharing remaining space proportionally with other flexible tracks. */
fun gridRowFlexible(value: Float): GridTrackSchema = GridTrackSchema.Flexible(value)

/** Row track sized automatically to fit its content. */
fun gridRowAuto(): GridTrackSchema = GridTrackSchema.Auto

/** Row track sized to the maximum content size among its cells. */
fun gridRowMaxContent(): GridTrackSchema = GridTrackSchema.MaxContent

/** Row track sized to the minimum content size among its cells. */
fun gridRowMinContent(): GridTrackSchema = GridTrackSchema.MinContent


/** Column track with a fixed size in dp. */
fun gridColumnFixed(value: Int): GridTrackSchema = GridTrackSchema.Fixed(value)

/** Column track sized as a fraction of the available space (0f–1f). */
fun gridColumnFraction(value: Float): GridTrackSchema = GridTrackSchema.Fraction(value)

/** Column track sized with an `fr`-flexible unit, sharing remaining space proportionally with other flexible tracks. */
fun gridColumnFlexible(value: Float): GridTrackSchema = GridTrackSchema.Flexible(value)

/** Column track sized automatically to fit its content. */
fun gridColumnAuto(): GridTrackSchema = GridTrackSchema.Auto

/** Column track sized to the maximum content size among its cells. */
fun gridColumnMaxContent(): GridTrackSchema = GridTrackSchema.MaxContent

/** Column track sized to the minimum content size among its cells. */
fun gridColumnMinContent(): GridTrackSchema = GridTrackSchema.MinContent
