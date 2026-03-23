package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.badges.badge

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.badges.BadgeTileSchema

object BadgeTileHolderBuilder : TileHolderBuilder<BadgeTileSchema, BadgeTileHolder> {

    override fun BuilderScope.build(
        tileModel: BadgeTileSchema
    ): BadgeTileHolder = with(tileModel) {
        BadgeTileHolder(
            id = id,
            tile = this,
            events = events?.buildEventHolders()
        )
    }
}
