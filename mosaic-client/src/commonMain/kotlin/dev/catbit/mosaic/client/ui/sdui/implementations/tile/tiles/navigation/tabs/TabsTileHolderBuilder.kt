package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.tabs

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.TabsTileSchema

object TabsTileHolderBuilder : TileHolderBuilder<TabsTileSchema, TabsTileHolder> {

    override fun BuilderScope.build(
        tileModel: TabsTileSchema
    ): TabsTileHolder = with(tileModel) {
        TabsTileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders()
        )
    }
}
