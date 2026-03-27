package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyTilesTileSchema

object LazyTilesTileHolderBuilder : TileHolderBuilder<LazyTilesTileSchema, LazyTilesTileHolder> {

    override fun BuilderScope.build(
        tileModel: LazyTilesTileSchema
    ) = with(tileModel) {
        LazyTilesTileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders(),
            placeholderTiles = placeholderTiles.buildTileHolders(),
            failureTiles = failureTiles.buildTileHolders()
        )
    }
}
