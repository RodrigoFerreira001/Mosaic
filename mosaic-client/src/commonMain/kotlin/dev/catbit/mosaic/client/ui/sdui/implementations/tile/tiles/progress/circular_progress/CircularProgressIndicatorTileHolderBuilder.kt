package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.progress.circular_progress

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.CircularProgressIndicatorTileSchema

object CircularProgressIndicatorTileHolderBuilder :
    TileHolderBuilder<CircularProgressIndicatorTileSchema, CircularProgressIndicatorTileHolder> {

    override fun BuilderScope.build(
        tileModel: CircularProgressIndicatorTileSchema
    ): CircularProgressIndicatorTileHolder = with(tileModel) {
        CircularProgressIndicatorTileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders()
        )
    }
}
