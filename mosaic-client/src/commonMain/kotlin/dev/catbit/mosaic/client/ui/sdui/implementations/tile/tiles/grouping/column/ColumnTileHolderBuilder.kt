package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.tile.tiles.grouping.ColumnTileModel

object ColumnTileHolderBuilder : TileHolderBuilder<ColumnTileModel, ColumnTileHolder> {

    override fun BuilderScope.build(
        tileModel: ColumnTileModel
    ) = with(tileModel) {
        ColumnTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList(),
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }.toMutableList()
        )
    }
}