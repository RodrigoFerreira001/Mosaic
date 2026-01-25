package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.tile.tiles.inputs.TextFieldTileModel

object TextFieldTileHolderBuilder : TileHolderBuilder<TextFieldTileModel, TextFieldTileHolder> {

    override fun BuilderScope.build(
        tileModel: TextFieldTileModel
    ) = with(tileModel) {
        TextFieldTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList()
        )
    }
}