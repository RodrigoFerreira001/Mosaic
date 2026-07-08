package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.popup

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.popup.PopupTileSchema

object PopupTileHolderBuilder : TileHolderBuilder<PopupTileSchema, PopupTileHolder> {

    override fun BuilderScope.build(
        tileModel: PopupTileSchema
    ) = with(tileModel) {
        PopupTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            anchorTiles = tiles.buildTileHolders(),
            popupTiles = popupTiles.buildTileHolders()
        )
    }
}
