package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.popup

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.popup.PopupTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class PopupTileHolder(
    override val id: String,
    override var tile: PopupTileSchema,
    override val events: MutableList<EventHolder<*>>,
    private val anchorTiles: List<TileHolder<*>>,
    private val popupTiles: List<TileHolder<*>>
) : TileHolder<PopupTileSchema>() {

    override val tiles: MutableList<TileHolder<*>> =
        (anchorTiles + popupTiles).toMutableList()

    override fun get() = tile.copy(
        tiles = anchorTiles.immutableMapTo { it.get() },
        popupTiles = popupTiles.immutableMapTo { it.get() },
        events = events.immutableMapTo { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is PopupTileEvents.OnTogglePopup -> {
                tile = tile.copy(expanded = !tile.expanded)
            }
        }
    }
}
