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

internal class FlexBoxTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
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

fun TileSchemaBuilderScope.FlexBox(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    searchableTerms: List<String>? = null,
    direction: FlexDirectionSchema = FlexDirectionSchema.Row,
    justifyContent: FlexJustifyContentSchema = FlexJustifyContentSchema.Start,
    alignItems: FlexAlignItemsSchema = FlexAlignItemsSchema.Start,
    alignContent: FlexAlignContentSchema = FlexAlignContentSchema.Start,
    wrap: FlexWrapSchema = FlexWrapSchema.NoWrap,
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

fun flexDirectionRow() = FlexDirectionSchema.Row
fun flexDirectionRowReverse() = FlexDirectionSchema.RowReverse
fun flexDirectionColumn() = FlexDirectionSchema.Column
fun flexDirectionColumnReverse() = FlexDirectionSchema.ColumnReverse

fun flexJustifyStart() = FlexJustifyContentSchema.Start
fun flexJustifyCenter() = FlexJustifyContentSchema.Center
fun flexJustifyEnd() = FlexJustifyContentSchema.End
fun flexJustifySpaceBetween() = FlexJustifyContentSchema.SpaceBetween
fun flexJustifySpaceAround() = FlexJustifyContentSchema.SpaceAround
fun flexJustifySpaceEvenly() = FlexJustifyContentSchema.SpaceEvenly

fun flexAlignItemsStart() = FlexAlignItemsSchema.Start
fun flexAlignItemsCenter() = FlexAlignItemsSchema.Center
fun flexAlignItemsEnd() = FlexAlignItemsSchema.End
fun flexAlignItemsStretch() = FlexAlignItemsSchema.Stretch
fun flexAlignItemsBaseline() = FlexAlignItemsSchema.Baseline

fun flexAlignContentStart() = FlexAlignContentSchema.Start
fun flexAlignContentCenter() = FlexAlignContentSchema.Center
fun flexAlignContentEnd() = FlexAlignContentSchema.End
fun flexAlignContentStretch() = FlexAlignContentSchema.Stretch
fun flexAlignContentSpaceBetween() = FlexAlignContentSchema.SpaceBetween
fun flexAlignContentSpaceAround() = FlexAlignContentSchema.SpaceAround

fun flexNoWrap() = FlexWrapSchema.NoWrap
fun flexWrap() = FlexWrapSchema.Wrap
fun flexWrapReverse() = FlexWrapSchema.WrapReverse
