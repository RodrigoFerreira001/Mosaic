package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.RowTileSchema

object RowTileHolderBuilder : TileHolderBuilder<RowTileSchema, RowTileHolder> {

    override fun BuilderScope.build(
        tileModel: RowTileSchema
    ) = with(tileModel) {
        RowTileHolder(
            id = id,
            tile = tileModel,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }?.toMutableList(),
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }.toMutableList()
        )
    }
}