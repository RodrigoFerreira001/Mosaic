package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.tile.tiles.text.TextTileModel

object TextTileHolderBuilder : TileHolderBuilder<TextTileModel, TextTileHolder> {

    override fun BuilderScope.build(
        tileModel: TextTileModel
    ) = with(tileModel) {
        TextTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList()
        )
    }
}