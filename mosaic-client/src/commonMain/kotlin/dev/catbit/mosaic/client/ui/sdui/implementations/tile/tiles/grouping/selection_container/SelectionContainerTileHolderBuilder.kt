package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.selection_container

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.SelectionContainerTileSchema

object SelectionContainerTileHolderBuilder : TileHolderBuilder<SelectionContainerTileSchema, SelectionContainerTileHolder> {

    override fun BuilderScope.build(
        tileModel: SelectionContainerTileSchema
    ) = with(tileModel) {
        SelectionContainerTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
