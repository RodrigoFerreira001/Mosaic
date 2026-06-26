package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.flow_row

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlowRowTileSchema

object FlowRowTileHolderBuilder : TileHolderBuilder<FlowRowTileSchema, FlowRowTileHolder> {

    override fun BuilderScope.build(tileModel: FlowRowTileSchema) = with(tileModel) {
        FlowRowTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
