package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.filter_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.FilterChipTileSchema

object FilterChipTileHolderBuilder : TileHolderBuilder<FilterChipTileSchema, FilterChipTileHolder> {

    override fun BuilderScope.build(
        tileModel: FilterChipTileSchema
    ): FilterChipTileHolder = with(tileModel) {
        FilterChipTileHolder(
            id = id,
            tile = this,
            events = events.buildEventHolders()
        )
    }
}
