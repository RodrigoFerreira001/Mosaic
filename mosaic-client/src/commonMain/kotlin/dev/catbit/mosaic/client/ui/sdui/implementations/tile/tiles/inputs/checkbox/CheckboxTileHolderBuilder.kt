package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.checkbox

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.CheckboxTileSchema

object CheckboxTileHolderBuilder : TileHolderBuilder<CheckboxTileSchema, CheckboxTileHolder> {

    override fun BuilderScope.build(
        tileModel: CheckboxTileSchema
    ): CheckboxTileHolder = with(tileModel) {
        CheckboxTileHolder(
            id = id,
            tile = this,
            events = events.buildEventHolders()
        )
    }
}
