package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.checkbox

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.CheckboxTileSchema

object CheckboxTileDefinition : TileDefinition<CheckboxTileSchema> {
    override val tileSchemaClass = CheckboxTileSchema::class
    override val tileRenderer = CheckboxTileRenderer
    override val tileHolderBuilder = CheckboxTileHolderBuilder
}
