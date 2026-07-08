package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.date_picker

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DatePickerTileSchema

object DatePickerTileDefinition : TileDefinition<DatePickerTileSchema> {
    override val tileSchemaClass = DatePickerTileSchema::class
    override val tileRenderer = DatePickerTileRenderer
    override val tileHolderBuilder = DatePickerTileHolderBuilder
}
