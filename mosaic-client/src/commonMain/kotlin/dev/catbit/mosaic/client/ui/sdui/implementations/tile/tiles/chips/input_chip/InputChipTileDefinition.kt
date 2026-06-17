package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.input_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.InputChipTileSchema

object InputChipTileDefinition : TileDefinition<InputChipTileSchema> {
    override val tileSchemaClass = InputChipTileSchema::class
    override val tileRenderer = InputChipTileRenderer
    override val tileHolderBuilder = InputChipTileHolderBuilder
}
