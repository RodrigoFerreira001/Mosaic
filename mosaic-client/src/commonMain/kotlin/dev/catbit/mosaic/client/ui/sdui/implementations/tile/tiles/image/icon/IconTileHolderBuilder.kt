package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.icon

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.IconTileSchema

object IconTileHolderBuilder : TileHolderBuilder<IconTileSchema, IconTileHolder> {

    override fun BuilderScope.build(
        tileModel: IconTileSchema
    ): IconTileHolder = with(tileModel) {
        IconTileHolder(
            id = id,
            tile = this,
            events = events?.buildEventHolders()
        )
    }
}
