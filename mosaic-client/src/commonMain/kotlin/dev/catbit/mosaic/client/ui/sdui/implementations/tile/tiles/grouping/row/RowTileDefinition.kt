package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.tile.tiles.containers.RowTileModel

object RowTileDefinition : TileDefinition<RowTileModel> {
    override val tileModelClass = RowTileModel::class
    override val tileRenderer = RowTileRenderer
    override val tileHolderBuilder = RowTileHolderBuilder
}