package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.icon

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.IconTileSchema

object IconTileDefinition : TileDefinition<IconTileSchema> {
    override val tileSchemaClass = IconTileSchema::class
    override val tileRenderer = IconTileRenderer
    override val tileHolderBuilder = IconTileHolderBuilder
}
