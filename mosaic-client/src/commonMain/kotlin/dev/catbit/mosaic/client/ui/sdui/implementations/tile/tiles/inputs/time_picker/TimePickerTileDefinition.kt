package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.time_picker

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TimePickerTileSchema

object TimePickerTileDefinition : TileDefinition<TimePickerTileSchema> {
    override val tileSchemaClass = TimePickerTileSchema::class
    override val tileRenderer = TimePickerTileRenderer
    override val tileHolderBuilder = TimePickerTileHolderBuilder
}
