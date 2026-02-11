package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.pager

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.PagerTileSchema

object PagerTileDefinition : TileDefinition<PagerTileSchema> {
    override val tileSchemaClass = PagerTileSchema::class
    override val tileRenderer = PagerTileRenderer
    override val tileHolderBuilder = PagerTileHolderBuilder
}
