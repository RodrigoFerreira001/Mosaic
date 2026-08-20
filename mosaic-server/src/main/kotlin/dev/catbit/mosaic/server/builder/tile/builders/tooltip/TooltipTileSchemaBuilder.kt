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
import dev.catbit.mosaic.server.builder.tile.visible

/** Positions the tooltip above its anchor. */
fun tooltipPositionAbove() = TooltipTileSchema.Position.ABOVE

/** Positions the tooltip below its anchor. */
fun tooltipPositionBelow() = TooltipTileSchema.Position.BELOW

/** Positions the tooltip to the left of its anchor, regardless of layout direction. */
fun tooltipPositionLeft() = TooltipTileSchema.Position.LEFT

/** Positions the tooltip to the right of its anchor, regardless of layout direction. */
fun tooltipPositionRight() = TooltipTileSchema.Position.RIGHT

/** Positions the tooltip to the leading edge of its anchor (mirrored under RTL). */
fun tooltipPositionStart() = TooltipTileSchema.Position.START

/** Positions the tooltip to the trailing edge of its anchor (mirrored under RTL). */
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

/**
 * Wraps [tiles] in a Material 3 plain tooltip showing [text]. The tooltip is driven entirely by
 * the platform's own gestures (long press on touch, hover on pointer devices) — the server
 * neither opens nor observes it. [position] places the tooltip relative to the anchor and
 * [spacing] (dp) sets the gap from it, defaulting to Material's own spacing when `null`.
 * [showCaret] toggles the little pointer arrow, and [maxWidth], [shape], [contentColor] and
 * [containerColor] each override the corresponding Material default when non-null. [style] and
 * [visibility] apply to the anchor, not to the tooltip surface, whose look is controlled by the
 * fields above. Only plain tooltips are supported — there is no rich variant with a title and
 * actions. Dispatches no triggers at all — wire events on the wrapped [tiles] instead.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param style Layout/appearance modifiers applied to the anchor (size, padding, background, etc).
 * @param visibility Whether the anchor is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param text Text shown inside the tooltip.
 * @param position Placement of the tooltip relative to the anchor. Defaults to above.
 * @param spacing Gap between the tooltip and the anchor, in dp. Defaults to none (Material's own spacing).
 * @param showCaret Whether to show the little pointer arrow. Defaults to false.
 * @param maxWidth Maximum width of the tooltip, in dp. Defaults to none (Material default).
 * @param shape Corner shape of the tooltip surface. Defaults to none (Material default).
 * @param contentColor Color of the tooltip's text. Defaults to none (Material default).
 * @param containerColor Background color of the tooltip surface. Defaults to none (Material default).
 * @param events Events owned by this tile. Never fired, since the tile dispatches no triggers.
 * @param tiles Child tiles forming the anchor the tooltip is attached to.
 */
fun TileSchemaBuilderScope.Tooltip(
    id: String = randomId(),
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
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
