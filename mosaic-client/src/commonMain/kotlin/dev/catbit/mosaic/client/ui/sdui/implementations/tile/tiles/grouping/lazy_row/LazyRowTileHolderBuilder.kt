package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_row

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyRowTileSchema

object LazyRowTileHolderBuilder : TileHolderBuilder<LazyRowTileSchema, LazyRowTileHolder> {

    override fun BuilderScope.build(
        tileModel: LazyRowTileSchema
    ) = with(tileModel) {
        LazyRowTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
