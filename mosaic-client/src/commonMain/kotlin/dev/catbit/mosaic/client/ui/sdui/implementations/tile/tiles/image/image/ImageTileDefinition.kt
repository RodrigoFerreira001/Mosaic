package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.image

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.ImageTileSchema

object ImageTileDefinition : TileDefinition<ImageTileSchema> {
    override val tileSchemaClass = ImageTileSchema::class
    override val tileRenderer = ImageTileRenderer
    override val tileHolderBuilder = ImageTileHolderBuilder
}
