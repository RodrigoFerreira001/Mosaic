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

fun TileSchemaBuilderScope.Popup(
    id: String = randomId(),
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
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
