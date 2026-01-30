package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema

object ButtonTileDefinition : TileDefinition<ButtonTileSchema> {
    override val tileSchemaClass = ButtonTileSchema::class
    override val tileRenderer = ButtonTileRenderer
    override val tileHolderBuilder = ButtonTileHolderBuilder
}