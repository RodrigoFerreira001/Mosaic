package dev.catbit.mosaic.sample.client.sdui.tiles.navigation.adaptive_navigation

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.sample.core.schemas.tiles.navigation.AdaptiveNavigationTileSchema

object AdaptiveNavigationTileHolderBuilder :
    TileHolderBuilder<AdaptiveNavigationTileSchema, AdaptiveNavigationTileHolder> {

    override fun BuilderScope.build(
        tileModel: AdaptiveNavigationTileSchema
    ): AdaptiveNavigationTileHolder = with(tileModel) {
        AdaptiveNavigationTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            primaryAction = primaryAction?.let { buildTileHolder(it) },
            topBarTitle = buildTileHolder(topBar.title),
            topBarSubtitle = topBar.subtitle?.buildTileHolders(),
            topBarActions = topBar.actions?.buildTileHolders(),
        )
    }
}
