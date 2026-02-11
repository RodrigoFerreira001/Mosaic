package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.box

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.BoxTileSchema

object BoxTileHolderBuilder : TileHolderBuilder<BoxTileSchema, BoxTileHolder> {

    override fun BuilderScope.build(
        tileModel: BoxTileSchema
    ) = with(tileModel) {
        BoxTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList(),
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }.toMutableList()
        )
    }
}
