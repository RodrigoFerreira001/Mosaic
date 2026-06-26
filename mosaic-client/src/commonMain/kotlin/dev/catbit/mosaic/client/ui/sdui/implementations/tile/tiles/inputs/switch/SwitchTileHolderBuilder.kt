package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.switch

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.SwitchTileSchema

object SwitchTileHolderBuilder : TileHolderBuilder<SwitchTileSchema, SwitchTileHolder> {

    override fun BuilderScope.build(
        tileModel: SwitchTileSchema
    ): SwitchTileHolder = with(tileModel) {
        SwitchTileHolder(
            id = id,
            tile = this,
            events = events.buildEventHolders()
        )
    }
}
