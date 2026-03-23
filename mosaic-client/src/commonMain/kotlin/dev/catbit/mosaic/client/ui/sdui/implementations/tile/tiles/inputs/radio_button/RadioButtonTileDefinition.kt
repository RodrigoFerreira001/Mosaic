package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.radio_button

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.RadioButtonTileSchema

object RadioButtonTileDefinition : TileDefinition<RadioButtonTileSchema> {
    override val tileSchemaClass = RadioButtonTileSchema::class
    override val tileRenderer = RadioButtonTileRenderer
    override val tileHolderBuilder = RadioButtonTileHolderBuilder
}
