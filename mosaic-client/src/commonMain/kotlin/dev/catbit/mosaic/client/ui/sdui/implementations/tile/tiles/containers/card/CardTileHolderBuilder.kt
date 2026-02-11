package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.card

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.CardTileSchema

object CardTileHolderBuilder : TileHolderBuilder<CardTileSchema, CardTileHolder> {

    override fun BuilderScope.build(
        tileModel: CardTileSchema
    ) = with(tileModel) {
        CardTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList(),
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }.toMutableList()
        )
    }
}
