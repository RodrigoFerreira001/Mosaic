package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.pager

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.PagerTileSchema

object PagerTileHolderBuilder : TileHolderBuilder<PagerTileSchema, PagerTileHolder> {

    override fun BuilderScope.build(
        tileModel: PagerTileSchema
    ) = with(tileModel) {
        PagerTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList(),
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }.toMutableList()
        )
    }
}
