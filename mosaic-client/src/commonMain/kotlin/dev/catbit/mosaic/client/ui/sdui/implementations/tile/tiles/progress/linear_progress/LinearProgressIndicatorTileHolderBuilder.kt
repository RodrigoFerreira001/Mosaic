package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.progress.linear_progress

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.LinearProgressIndicatorTileSchema

object LinearProgressIndicatorTileHolderBuilder : TileHolderBuilder<LinearProgressIndicatorTileSchema, LinearProgressIndicatorTileHolder> {

    override fun BuilderScope.build(
        tileModel: LinearProgressIndicatorTileSchema
    ): LinearProgressIndicatorTileHolder = with(tileModel) {
        LinearProgressIndicatorTileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders()
        )
    }
}
