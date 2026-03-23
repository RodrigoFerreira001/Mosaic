package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.grid

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.GridTileSchema

object GridTileDefinition : TileDefinition<GridTileSchema> {
    override val tileSchemaClass = GridTileSchema::class
    override val tileRenderer = GridTileRenderer
    override val tileHolderBuilder = GridTileHolderBuilder
}
