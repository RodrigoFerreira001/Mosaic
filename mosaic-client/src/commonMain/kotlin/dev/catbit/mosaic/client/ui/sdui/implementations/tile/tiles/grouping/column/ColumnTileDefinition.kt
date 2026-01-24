package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.tile.tiles.grouping.ColumnTileModel

object ColumnTileDefinition : TileDefinition<ColumnTileModel> {
    override val tileModelClass = ColumnTileModel::class
    override val tileRenderer = ColumnTileRenderer
    override val tileHolderBuilder = ColumnTileHolderBuilder
}