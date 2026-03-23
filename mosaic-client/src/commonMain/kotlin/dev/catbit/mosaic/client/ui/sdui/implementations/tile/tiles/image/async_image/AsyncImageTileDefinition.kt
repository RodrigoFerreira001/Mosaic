package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.async_image

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema

object AsyncImageTileDefinition : TileDefinition<AsyncImageTileSchema> {
    override val tileSchemaClass = AsyncImageTileSchema::class
    override val tileRenderer = AsyncImageTileRenderer
    override val tileHolderBuilder = AsyncImageTileHolderBuilder
}
