package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.shimmer

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.ShimmerTileSchema

object ShimmerTileHolderBuilder : TileHolderBuilder<ShimmerTileSchema, ShimmerTileHolder> {

    override fun BuilderScope.build(
        tileModel: ShimmerTileSchema
    ) = with(tileModel) {
        ShimmerTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
