package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.suggestion_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.SuggestionChipTileSchema

object SuggestionChipTileHolderBuilder : TileHolderBuilder<SuggestionChipTileSchema, SuggestionChipTileHolder> {

    override fun BuilderScope.build(
        tileModel: SuggestionChipTileSchema
    ): SuggestionChipTileHolder = with(tileModel) {
        SuggestionChipTileHolder(
            id = id,
            tile = this,
            events = events?.buildEventHolders()
        )
    }
}
