package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.input_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.InputChipTileSchema

object InputChipTileHolderBuilder : TileHolderBuilder<InputChipTileSchema, InputChipTileHolder> {

    override fun BuilderScope.build(
        tileModel: InputChipTileSchema
    ): InputChipTileHolder = with(tileModel) {
        InputChipTileHolder(
            id = id,
            tile = this,
            events = events?.buildEventHolders()
        )
    }
}
