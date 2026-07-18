package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.tooltip

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.tooltip.TooltipTileSchema

object TooltipTileHolderBuilder : TileHolderBuilder<TooltipTileSchema, TooltipTileHolder> {

    override fun BuilderScope.build(
        tileModel: TooltipTileSchema
    ) = with(tileModel) {
        TooltipTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
