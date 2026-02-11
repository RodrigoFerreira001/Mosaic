package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.box

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.BoxTileSchema

object BoxTileDefinition : TileDefinition<BoxTileSchema> {
    override val tileSchemaClass = BoxTileSchema::class
    override val tileRenderer = BoxTileRenderer
    override val tileHolderBuilder = BoxTileHolderBuilder
}
