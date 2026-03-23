package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.simple_text

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.SimpleTextTileSchema

object SimpleTextTileDefinition : TileDefinition<SimpleTextTileSchema> {
    override val tileSchemaClass = SimpleTextTileSchema::class
    override val tileRenderer = SimpleTextTileRenderer
    override val tileHolderBuilder = SimpleTextTileHolderBuilder
}
