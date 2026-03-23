package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.progress.linear_progress

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.LinearProgressIndicatorTileSchema

object LinearProgressIndicatorTileDefinition : TileDefinition<LinearProgressIndicatorTileSchema> {
    override val tileSchemaClass = LinearProgressIndicatorTileSchema::class
    override val tileRenderer = LinearProgressIndicatorTileRenderer
    override val tileHolderBuilder = LinearProgressIndicatorTileHolderBuilder
}
