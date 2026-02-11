package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.carousel

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.CarouselTileSchema

object CarouselTileHolderBuilder : TileHolderBuilder<CarouselTileSchema, CarouselTileHolder> {

    override fun BuilderScope.build(
        tileModel: CarouselTileSchema
    ) = with(tileModel) {
        CarouselTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList(),
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }.toMutableList()
        )
    }
}
