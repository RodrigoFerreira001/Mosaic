package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.menu.MenuTileSchema

class MenuTileHolder(
    override val id: String,
    override var tile: MenuTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<MenuTileSchema>() {

    override fun get() = tile.copy(
        tiles = tiles.map { it.get() },
        events = events?.map { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is MenuTileEvents.OnToggleMenu -> {
                tile = tile.copy(expanded = !tile.expanded)
            }
        }
    }
}