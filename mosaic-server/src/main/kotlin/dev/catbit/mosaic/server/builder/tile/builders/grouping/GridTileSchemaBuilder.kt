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
import kotlinx.collections.immutable.toImmutableList

internal class GridTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
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
            visibility = visibility,
            columns = columns.toImmutableList(),
            rows = rows.toImmutableList(),
            columnGap = columnGap,
            rowGap = rowGap,
            flow = flow
        )
    }
}

fun TileSchemaBuilderScope.Grid(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    flow: GridFlowSchema = GridFlowSchema.Row,
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
            visibility = visibility,
            flow = flow,
            columns = columns,
            rows = rows,
            columnGap = columnGap,
            rowGap = rowGap
        )
    )
}

fun flowGridThroughRows() = GridFlowSchema.Row
fun flowGridThroughColumns() = GridFlowSchema.Column

fun gridRowFixed(value: Int): GridTrackSchema = GridTrackSchema.Fixed(value)
fun gridRowFraction(value: Float): GridTrackSchema = GridTrackSchema.Fraction(value)
fun gridRowFlexible(value: Float): GridTrackSchema = GridTrackSchema.Flexible(value)
fun gridRowAuto(): GridTrackSchema = GridTrackSchema.Auto
fun gridRowMaxContent(): GridTrackSchema = GridTrackSchema.MaxContent
fun gridRowMinContent(): GridTrackSchema = GridTrackSchema.MinContent

fun gridColumnFixed(value: Int): GridTrackSchema = GridTrackSchema.Fixed(value)
fun gridColumnFraction(value: Float): GridTrackSchema = GridTrackSchema.Fraction(value)
fun gridColumnFlexible(value: Float): GridTrackSchema = GridTrackSchema.Flexible(value)
fun gridColumnAuto(): GridTrackSchema = GridTrackSchema.Auto
fun gridColumnMaxContent(): GridTrackSchema = GridTrackSchema.MaxContent
fun gridColumnMinContent(): GridTrackSchema = GridTrackSchema.MinContent
