package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.assist_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.AssistChipTileSchema

object AssistChipTileDefinition : TileDefinition<AssistChipTileSchema> {
    override val tileSchemaClass = AssistChipTileSchema::class
    override val tileRenderer = AssistChipTileRenderer
    override val tileHolderBuilder = AssistChipTileHolderBuilder
}
