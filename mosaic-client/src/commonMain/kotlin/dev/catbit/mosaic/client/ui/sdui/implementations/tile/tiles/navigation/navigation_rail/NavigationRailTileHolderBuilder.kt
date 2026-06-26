package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_rail

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationRailTileSchema

object NavigationRailTileHolderBuilder : TileHolderBuilder<NavigationRailTileSchema, NavigationRailTileHolder> {

    override fun BuilderScope.build(
        tileModel: NavigationRailTileSchema
    ): NavigationRailTileHolder = with(tileModel) {
        NavigationRailTileHolder(
            id = id,
            tile = this,
            events = events.buildEventHolders(),
            header = header?.let { buildTileHolder(it) },
            footer = header?.let { buildTileHolder(it) }
        )
    }
}
