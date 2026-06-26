package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.search.search_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.search.SearchBarTileSchema

object SearchBarTileHolderBuilder : TileHolderBuilder<SearchBarTileSchema, SearchBarTileHolder> {

    override fun BuilderScope.build(
        tileModel: SearchBarTileSchema
    ): SearchBarTileHolder = with(tileModel) {
        SearchBarTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            leadingIconHolder = leadingIcon?.let { buildTileHolder(it) },
            trailingIconHolder = trailingIcon?.let { buildTileHolder(it) }
        )
    }
}
