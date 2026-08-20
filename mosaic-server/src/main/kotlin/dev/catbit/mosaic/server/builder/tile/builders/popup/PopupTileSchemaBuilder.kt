package dev.catbit.mosaic.server.builder.tile.builders.popup

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.popup.PopupTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.placement.alignToTopStart
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class PopupTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val popupTiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val expanded: Boolean,
    private val alignment: AlignmentSchema.TwoDimensional,
    private val offsetX: Int,
    private val offsetY: Int,
    private val focusable: Boolean,
    private val dismissOnBackPress: Boolean,
    private val dismissOnClickOutside: Boolean
) : TileSchemaBuilder<PopupTileSchema>() {

    override fun build() = PopupTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        popupTiles = TileSchemaBuilderScope().apply(popupTiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        expanded = expanded,
        alignment = alignment,
        offsetX = offsetX,
        offsetY = offsetY,
        focusable = focusable,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside
    )
}

/**
 * Renders an anchor — [tiles], laid out with `Box` semantics and carrying [style] and
 * [visibility] — with a floating popup containing [popupTiles] over it. The popup is only
 * composed while [expanded] is `true`. [alignment] places the popup relative to the anchor's
 * bounds, and [offsetX] / [offsetY] (in dp) are applied on top of that: top/bottom alignments put
 * the popup fully above/below the anchor with [offsetY] as the gap, start/end-center alignments
 * put it fully to the side with [offsetX] as the gap (mirrored under RTL), and corner alignments
 * flush-align the popup's matching edge with the anchor's, with [offsetX] as a plain translation.
 * The final position is clamped to the window, so the popup can never be pushed off screen.
 * [focusable], [dismissOnBackPress] and [dismissOnClickOutside] control how it can be dismissed.
 * A dismissal (back press, tap outside) flips [expanded] locally on the client, with no server
 * round trip needed; opening and closing from the server side is done with a `TogglePopup` event
 * pointing at this tile's [id] — typically wired to the anchor's click. [style] and [visibility]
 * apply to the anchor only — the popup renders in its own window, unaffected by them, and its
 * content is unstyled and undecorated, so wrap [popupTiles] in a `Card` or a styled `Box` for a
 * surface behind it. Dispatches no triggers at all — wire events on the anchor and popup tiles
 * instead.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param style Layout/appearance modifiers applied to the anchor (size, padding, background, etc).
 * @param visibility Whether the anchor is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param expanded Whether the popup starts open. Defaults to false.
 * @param alignment Placement of the popup relative to the anchor's bounds. Defaults to top start.
 * @param offsetX Horizontal offset applied on top of [alignment], in dp. Defaults to 0.
 * @param offsetY Vertical offset applied on top of [alignment], in dp. Defaults to 0.
 * @param focusable Whether the popup can take focus (e.g. for text input inside it). Defaults to false.
 * @param dismissOnBackPress Whether pressing back dismisses the popup. Defaults to true.
 * @param dismissOnClickOutside Whether tapping outside the popup dismisses it. Defaults to true.
 * @param events Events owned by this tile. Never fired, since the tile dispatches no triggers.
 * @param tiles Child tiles forming the anchor the popup is attached to.
 * @param popupTiles Child tiles rendered inside the floating popup.
 */
fun TileSchemaBuilderScope.Popup(
    id: String = randomId(),
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    expanded: Boolean = false,
    alignment: AlignmentSchema.TwoDimensional = alignToTopStart(),
    offsetX: Int = 0,
    offsetY: Int = 0,
    focusable: Boolean = false,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tiles: TileSchemaBuilderScope.() -> Unit,
    popupTiles: TileSchemaBuilderScope.() -> Unit,
) {
    addBuilder(
        PopupTileSchemaBuilder(
            id = id,
            tiles = tiles,
            popupTiles = popupTiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            expanded = expanded,
            alignment = alignment,
            offsetX = offsetX,
            offsetY = offsetY,
            focusable = focusable,
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    )
}
