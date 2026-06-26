package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.icon_button

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.IconButtonTileSchema

object IconButtonTileHolderBuilder : TileHolderBuilder<IconButtonTileSchema, IconButtonTileHolder> {

    override fun BuilderScope.build(
        tileModel: IconButtonTileSchema
    ): IconButtonTileHolder = with(tileModel) {
        IconButtonTileHolder(
            id = id,
            tile = this,
            events = events.buildEventHolders()
        )
    }
}
