package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.async_image

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema

object AsyncImageTileHolderBuilder : TileHolderBuilder<AsyncImageTileSchema, AsyncImageTileHolder> {

    override fun BuilderScope.build(
        tileModel: AsyncImageTileSchema
    ): AsyncImageTileHolder = with(tileModel) {
        AsyncImageTileHolder(
            id = id,
            tile = this,
            events = events.buildEventHolders()
        )
    }
}
