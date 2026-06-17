package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.adaptive_visibility

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema

object AdaptiveVisibilityTileHolderBuilder :
    TileHolderBuilder<AdaptiveVisibilityTileSchema, AdaptiveVisibilityTileHolder> {

    override fun BuilderScope.build(
        tileModel: AdaptiveVisibilityTileSchema
    ) = with(tileModel) {
        AdaptiveVisibilityTileHolder(
            id = id,
            tile = tileModel,
            events = events?.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
