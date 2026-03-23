package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.ColumnTileSchema

object ColumnTileDefinition : TileDefinition<ColumnTileSchema> {
    override val tileSchemaClass = ColumnTileSchema::class
    override val tileRenderer = ColumnTileRenderer
    override val tileHolderBuilder = ColumnTileHolderBuilder
}