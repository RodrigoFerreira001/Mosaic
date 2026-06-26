package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema

object ButtonTileHolderBuilder : TileHolderBuilder<ButtonTileSchema, ButtonTileHolder> {

    override fun BuilderScope.build(
        tileModel: ButtonTileSchema
    ): ButtonTileHolder = with(tileModel) {
        ButtonTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders()
        )
    }
}