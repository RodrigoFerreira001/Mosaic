package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.radio_button

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.RadioButtonTileSchema

object RadioButtonTileHolderBuilder : TileHolderBuilder<RadioButtonTileSchema, RadioButtonTileHolder> {

    override fun BuilderScope.build(
        tileModel: RadioButtonTileSchema
    ): RadioButtonTileHolder = with(tileModel) {
        RadioButtonTileHolder(
            id = id,
            tile = this,
            events = events?.buildEventHolders()
        )
    }
}
