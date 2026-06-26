package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.assist_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.AssistChipTileSchema

object AssistChipTileHolderBuilder : TileHolderBuilder<AssistChipTileSchema, AssistChipTileHolder> {

    override fun BuilderScope.build(
        tileModel: AssistChipTileSchema
    ): AssistChipTileHolder = with(tileModel) {
        AssistChipTileHolder(
            id = id,
            tile = this,
            events = events.buildEventHolders()
        )
    }
}
