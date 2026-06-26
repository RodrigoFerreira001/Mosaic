package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.image

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.ImageTileSchema

object ImageTileHolderBuilder : TileHolderBuilder<ImageTileSchema, ImageTileHolder> {
    override fun BuilderScope.build(
        tileModel: ImageTileSchema
    ): ImageTileHolder = with(tileModel) {
        ImageTileHolder(
            id = id,
            tile = this,
            events = events.buildEventHolders()
        )
    }
}
