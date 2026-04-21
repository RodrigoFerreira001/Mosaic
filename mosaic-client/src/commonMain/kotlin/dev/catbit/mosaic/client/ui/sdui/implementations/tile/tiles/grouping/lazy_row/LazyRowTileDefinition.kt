package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_row

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyRowTileSchema

object LazyRowTileDefinition : TileDefinition<LazyRowTileSchema> {
    override val tileSchemaClass = LazyRowTileSchema::class
    override val tileRenderer = LazyRowTileRenderer
    override val tileHolderBuilder = LazyRowTileHolderBuilder
}
