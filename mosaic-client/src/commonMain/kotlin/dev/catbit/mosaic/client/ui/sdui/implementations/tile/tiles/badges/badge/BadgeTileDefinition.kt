package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.badges.badge

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.badges.BadgeTileSchema

object BadgeTileDefinition : TileDefinition<BadgeTileSchema> {
    override val tileSchemaClass = BadgeTileSchema::class
    override val tileRenderer = BadgeTileRenderer
    override val tileHolderBuilder = BadgeTileHolderBuilder
}
