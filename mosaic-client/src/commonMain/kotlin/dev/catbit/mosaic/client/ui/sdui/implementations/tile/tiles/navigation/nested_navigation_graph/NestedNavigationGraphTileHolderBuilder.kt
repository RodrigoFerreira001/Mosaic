package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.nested_navigation_graph

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NestedNavigationGraphTileSchema

object NestedNavigationGraphTileHolderBuilder : TileHolderBuilder<NestedNavigationGraphTileSchema, NestedNavigationGraphTileHolder> {

    override fun BuilderScope.build(
        tileModel: NestedNavigationGraphTileSchema
    ): NestedNavigationGraphTileHolder = with(tileModel) {
        NestedNavigationGraphTileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders()
        )
    }
}
