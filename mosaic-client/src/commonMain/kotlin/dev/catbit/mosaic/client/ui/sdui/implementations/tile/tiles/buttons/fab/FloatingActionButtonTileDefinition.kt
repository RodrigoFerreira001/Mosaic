package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.fab

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.FloatingActionButtonTileSchema

object FloatingActionButtonTileDefinition : TileDefinition<FloatingActionButtonTileSchema> {
    override val tileSchemaClass = FloatingActionButtonTileSchema::class
    override val tileRenderer = FloatingActionButtonTileRenderer
    override val tileHolderBuilder = FloatingActionButtonTileHolderBuilder
}
