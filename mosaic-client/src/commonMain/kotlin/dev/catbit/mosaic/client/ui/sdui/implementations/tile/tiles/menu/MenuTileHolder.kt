package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolder
import dev.catbit.mosaic.core.data.tile.tiles.menu.MenuTileModel

class MenuTileHolder(
    override val id: String,
    override var tile: MenuTileModel,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<MenuTileModel>() {

    override fun get() = tile.copy(
        tiles = tiles.map { it.get() },
        events = events?.map { it.get() }
    )

    override fun onTileEvent(event: TileEvent) {
        when (event) {
            is MenuTileEvents.OnToggleMenu -> {
                tile = tile.copy(expanded = !tile.expanded)
//                updateState()
            }
        }
    }
}