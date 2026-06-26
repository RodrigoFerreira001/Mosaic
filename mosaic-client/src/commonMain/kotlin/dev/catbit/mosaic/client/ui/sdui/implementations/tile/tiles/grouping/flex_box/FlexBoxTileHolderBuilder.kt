package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.flex_box

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema

object FlexBoxTileHolderBuilder : TileHolderBuilder<FlexBoxTileSchema, FlexBoxTileHolder> {

    override fun BuilderScope.build(tileModel: FlexBoxTileSchema) = with(tileModel) {
        FlexBoxTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
