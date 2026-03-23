package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.grid

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.GridTileSchema

object GridTileHolderBuilder : TileHolderBuilder<GridTileSchema, GridTileHolder> {

    override fun BuilderScope.build(
        tileModel: GridTileSchema
    ) = with(tileModel) {
        GridTileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
