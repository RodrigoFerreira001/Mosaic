package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema

object TextFieldTileDefinition : TileDefinition<TextFieldTileSchema> {
    override val tileSchemaClass = TextFieldTileSchema::class
    override val tileRenderer = TextFieldTileRenderer
    override val tileHolderBuilder = TextFieldTileHolderBuilder
}