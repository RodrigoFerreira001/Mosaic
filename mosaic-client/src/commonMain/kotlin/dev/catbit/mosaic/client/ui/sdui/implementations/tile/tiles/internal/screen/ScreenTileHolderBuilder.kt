package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder

object ScreenTileHolderBuilder : TileHolderBuilder<ScreenTileSchema, ScreenTileHolder> {

    override fun BuilderScope.build(
        tileModel: ScreenTileSchema
    ) = with(tileModel) {
        ScreenTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            tiles = tiles.buildTileHolders(),
            navigationDrawerTiles = navigationDrawerTiles?.buildTileHolders(),
        )
    }
}