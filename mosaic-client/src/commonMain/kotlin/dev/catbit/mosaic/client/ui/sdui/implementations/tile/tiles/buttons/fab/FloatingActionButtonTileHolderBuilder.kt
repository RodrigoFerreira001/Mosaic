package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.fab

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.FloatingActionButtonTileSchema

object FloatingActionButtonTileHolderBuilder :
    TileHolderBuilder<FloatingActionButtonTileSchema, FloatingActionButtonTileHolder> {

    override fun BuilderScope.build(
        tileModel: FloatingActionButtonTileSchema
    ): FloatingActionButtonTileHolder = with(tileModel) {
        FloatingActionButtonTileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders(),
        )
    }
}
