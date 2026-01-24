package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolderBuilder

object ScreenTileHolderBuilder : TileHolderBuilder<ScreenTileModel, ScreenTileHolder> {

    override fun BuilderScope.build(
        tileModel: ScreenTileModel
    ) = with(tileModel) {
        ScreenTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList(),
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }.toMutableList(),
            navigationDrawerTiles = navigationDrawerTiles?.map { tileModel -> buildTileHolder(tileModel) },
        )
    }
}