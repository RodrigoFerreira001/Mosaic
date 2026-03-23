package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.app_bars.bottom_app_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.BottomAppBarTileSchema

object BottomAppBarTileHolderBuilder : TileHolderBuilder<BottomAppBarTileSchema, BottomAppBarTileHolder> {

    override fun BuilderScope.build(
        tileModel: BottomAppBarTileSchema
    ): BottomAppBarTileHolder = with(tileModel) {
        BottomAppBarTileHolder(
            id = id,
            tile = this,
            events = events?.buildEventHolders(),
            actions = actions.buildTileHolders(),
            floatingActionButton = floatingActionButton?.let { buildTileHolder(it) }
        )
    }
}
