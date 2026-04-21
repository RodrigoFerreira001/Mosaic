package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_column

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyColumnTileSchema

object LazyColumnTileDefinition : TileDefinition<LazyColumnTileSchema> {
    override val tileSchemaClass = LazyColumnTileSchema::class
    override val tileRenderer = LazyColumnTileRenderer
    override val tileHolderBuilder = LazyColumnTileHolderBuilder
}
