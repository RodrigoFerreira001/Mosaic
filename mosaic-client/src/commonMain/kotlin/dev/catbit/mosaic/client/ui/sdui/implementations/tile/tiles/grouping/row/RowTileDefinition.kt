package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.RowTileSchema

object RowTileDefinition : TileDefinition<RowTileSchema> {
    override val tileSchemaClass = RowTileSchema::class
    override val tileRenderer = RowTileRenderer
    override val tileHolderBuilder = RowTileHolderBuilder
}