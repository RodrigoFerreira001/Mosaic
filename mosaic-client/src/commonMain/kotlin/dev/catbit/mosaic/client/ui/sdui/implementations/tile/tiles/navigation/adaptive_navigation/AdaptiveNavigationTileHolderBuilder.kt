package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.adaptive_navigation

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.AdaptiveNavigationTileSchema

object AdaptiveNavigationTileHolderBuilder : TileHolderBuilder<AdaptiveNavigationTileSchema, AdaptiveNavigationTileHolder> {

    override fun BuilderScope.build(
        tileModel: AdaptiveNavigationTileSchema
    ): AdaptiveNavigationTileHolder = with(tileModel) {
        AdaptiveNavigationTileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders(),
            header = header?.let { buildTileHolder(it) },
            footer = footer?.let { buildTileHolder(it) },
        )
    }
}
