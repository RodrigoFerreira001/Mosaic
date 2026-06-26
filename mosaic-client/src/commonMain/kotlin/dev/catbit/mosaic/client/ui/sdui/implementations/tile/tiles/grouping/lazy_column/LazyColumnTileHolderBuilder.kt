package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_column

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyColumnTileSchema

object LazyColumnTileHolderBuilder : TileHolderBuilder<LazyColumnTileSchema, LazyColumnTileHolder> {

    override fun BuilderScope.build(
        tileModel: LazyColumnTileSchema
    ) = with(tileModel) {
        LazyColumnTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
