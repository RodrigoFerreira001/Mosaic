package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.pull_to_refresh

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.PullToRefreshTileSchema

class PullToRefreshTileHolder(
    override val id: String,
    override var tile: PullToRefreshTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<PullToRefreshTileSchema>() {

    override fun get() = tile.copy(
        tiles = tiles.map { it.get() },
        events = events?.map { it.get() }
    )
}
