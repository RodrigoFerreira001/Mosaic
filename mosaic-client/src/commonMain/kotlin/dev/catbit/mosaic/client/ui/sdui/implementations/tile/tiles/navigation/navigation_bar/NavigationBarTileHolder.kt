package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationBarTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class NavigationBarTileHolder(
    override val id: String,
    override var tile: NavigationBarTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<NavigationBarTileSchema>() {

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() },
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is NavigationBarTileEvents.OnItemClicked -> {
                tile = tile.copy(
                    selectedItemId = event.itemId
                )
            }
        }
    }
}
