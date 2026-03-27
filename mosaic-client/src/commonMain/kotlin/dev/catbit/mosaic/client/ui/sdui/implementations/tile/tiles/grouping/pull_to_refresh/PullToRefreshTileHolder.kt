package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pull_to_refresh

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PullToRefreshTileSchema

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

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is PullToRefreshTileEvents.OnRefreshStart -> {
                tile = tile.copy(isRefreshing = true)
            }

            PullToRefreshTileEvents.StopRefreshing -> {
                tile = tile.copy(isRefreshing = false)
            }
        }
    }
}
