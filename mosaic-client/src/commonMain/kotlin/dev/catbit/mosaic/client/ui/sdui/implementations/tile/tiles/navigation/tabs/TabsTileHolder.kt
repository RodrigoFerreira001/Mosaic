package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.tabs

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.TabsTileSchema

class TabsTileHolder(
    override val id: String,
    override var tile: TabsTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<TabsTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() },
    )
}
