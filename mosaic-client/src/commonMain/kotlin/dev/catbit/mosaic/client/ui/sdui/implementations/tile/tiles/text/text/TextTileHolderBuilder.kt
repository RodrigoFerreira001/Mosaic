package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.TextTileSchema

object TextTileHolderBuilder : TileHolderBuilder<TextTileSchema, TextTileHolder> {

    override fun BuilderScope.build(
        tileModel: TextTileSchema
    ) = with(tileModel) {
        TextTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList()
        )
    }
}