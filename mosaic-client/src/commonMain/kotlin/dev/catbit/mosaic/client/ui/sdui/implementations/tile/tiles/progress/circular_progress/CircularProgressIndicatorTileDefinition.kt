package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.progress.circular_progress

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.CircularProgressIndicatorTileSchema

object CircularProgressIndicatorTileDefinition : TileDefinition<CircularProgressIndicatorTileSchema> {
    override val tileSchemaClass = CircularProgressIndicatorTileSchema::class
    override val tileRenderer = CircularProgressIndicatorTileRenderer
    override val tileHolderBuilder = CircularProgressIndicatorTileHolderBuilder
}
