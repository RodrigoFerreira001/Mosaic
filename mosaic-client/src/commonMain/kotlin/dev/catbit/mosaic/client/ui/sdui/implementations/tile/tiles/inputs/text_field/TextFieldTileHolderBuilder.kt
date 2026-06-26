package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema

object TextFieldTileHolderBuilder : TileHolderBuilder<TextFieldTileSchema, TextFieldTileHolder> {

    override fun BuilderScope.build(
        tileModel: TextFieldTileSchema
    ): TextFieldTileHolder = with(tileModel) {
        TextFieldTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders()
        )
    }
}