package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.grid

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.GridTileSchema

object GridTileHolderBuilder : TileHolderBuilder<GridTileSchema, GridTileHolder> {

    override fun BuilderScope.build(
        tileModel: GridTileSchema
    ) = with(tileModel) {
        GridTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList(),
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }.toMutableList()
        )
    }
}
