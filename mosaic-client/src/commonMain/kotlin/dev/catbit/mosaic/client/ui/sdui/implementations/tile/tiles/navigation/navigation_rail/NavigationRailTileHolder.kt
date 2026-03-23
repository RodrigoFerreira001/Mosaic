package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_rail

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationRailTileSchema

class NavigationRailTileHolder(
    override val id: String,
    override var tile: NavigationRailTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    private val header: TileHolder<*>?,
    private val footer: TileHolder<*>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<NavigationRailTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}
