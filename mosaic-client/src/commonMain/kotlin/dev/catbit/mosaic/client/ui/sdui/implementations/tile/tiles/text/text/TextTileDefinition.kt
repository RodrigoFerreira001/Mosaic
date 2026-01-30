package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.TextTileSchema

object TextTileDefinition : TileDefinition<TextTileSchema> {
    override val tileSchemaClass = TextTileSchema::class
    override val tileRenderer = TextTileRenderer
    override val tileHolderBuilder = TextTileHolderBuilder
}