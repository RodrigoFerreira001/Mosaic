package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.app_bars.top_app_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.TopAppBarTileSchema

object TopAppBarTileHolderBuilder : TileHolderBuilder<TopAppBarTileSchema, TopAppBarTileHolder> {

    override fun BuilderScope.build(
        tileModel: TopAppBarTileSchema
    ): TopAppBarTileHolder = with(tileModel) {
        TopAppBarTileHolder(
            id = id,
            tile = this,
            events = events.buildEventHolders(),
            title = buildTileHolder(title),
            navigationIcon = navigationIcon?.let { buildTileHolder(it)},
            actions = actions?.buildTileHolders(),
        )
    }
}
