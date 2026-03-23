package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationBarTileSchema

object NavigationBarTileHolderBuilder : TileHolderBuilder<NavigationBarTileSchema, NavigationBarTileHolder> {

    override fun BuilderScope.build(
        tileModel: NavigationBarTileSchema
    ): NavigationBarTileHolder = with(tileModel) {
        NavigationBarTileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders()
        )
    }
}
