package dev.catbit.mosaic.server.builder.tile.builders.tooltip

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.ShapeSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.tooltip.TooltipTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

fun tooltipPositionAbove() = TooltipTileSchema.Position.ABOVE

fun tooltipPositionBelow() = TooltipTileSchema.Position.BELOW

fun tooltipPositionLeft() = TooltipTileSchema.Position.LEFT

fun tooltipPositionRight() = TooltipTileSchema.Position.RIGHT

fun tooltipPositionStart() = TooltipTileSchema.Position.START

fun tooltipPositionEnd() = TooltipTileSchema.Position.END

internal class TooltipTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val text: String,
    private val position: TooltipTileSchema.Position,
    private val spacing: Int?,
    private val showCaret: Boolean,
    private val maxWidth: Int?,
    private val shape: ShapeSchema?,
    private val contentColor: ColorSchema?,
    private val containerColor: ColorSchema?
) : TileSchemaBuilder<TooltipTileSchema>() {

    override fun build() = TooltipTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        text = text,
        position = position,
        spacing = spacing,
        showCaret = showCaret,
        maxWidth = maxWidth,
        shape = shape,
        contentColor = contentColor,
        containerColor = containerColor
    )
}

fun TileSchemaBuilderScope.Tooltip(
    id: String = randomId(),
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    searchableTerms: List<String>? = null,
    text: String,
    position: TooltipTileSchema.Position = tooltipPositionAbove(),
    spacing: Int? = null,
    showCaret: Boolean = false,
    maxWidth: Int? = null,
    shape: ShapeSchema? = null,
    contentColor: ColorSchema? = null,
    containerColor: ColorSchema? = null,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tiles: TileSchemaBuilderScope.() -> Unit,
) {
    addBuilder(
        TooltipTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            text = text,
            position = position,
            spacing = spacing,
            showCaret = showCaret,
            maxWidth = maxWidth,
            shape = shape,
            contentColor = contentColor,
            containerColor = containerColor
        )
    )
}
