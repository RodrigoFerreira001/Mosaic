package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.tile.tiles.buttons.ButtonTileModel

object ButtonTileHolderBuilder : TileHolderBuilder<ButtonTileModel, ButtonTileHolder> {

    override fun BuilderScope.build(
        tileModel: ButtonTileModel
    ): ButtonTileHolder = with(tileModel) {
        ButtonTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList()
        )
    }
}