package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyTilesTileSchema

object LazyTilesTileDefinition : TileDefinition<LazyTilesTileSchema> {
    override val tileSchemaClass = LazyTilesTileSchema::class
    override val tileRenderer = LazyTilesTileRenderer
    override val tileHolderBuilder = LazyTilesTileHolderBuilder
}
