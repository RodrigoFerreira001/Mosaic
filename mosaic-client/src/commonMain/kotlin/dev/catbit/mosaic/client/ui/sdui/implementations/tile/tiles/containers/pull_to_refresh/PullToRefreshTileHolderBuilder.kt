package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.pull_to_refresh

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.PullToRefreshTileSchema

object PullToRefreshTileHolderBuilder : TileHolderBuilder<PullToRefreshTileSchema, PullToRefreshTileHolder> {

    override fun BuilderScope.build(
        tileModel: PullToRefreshTileSchema
    ) = with(tileModel) {
        PullToRefreshTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList(),
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }.toMutableList()
        )
    }
}
