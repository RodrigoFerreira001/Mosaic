package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pager

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PagerTileSchema

object PagerTileDefinition : TileDefinition<PagerTileSchema> {
    override val tileSchemaClass = PagerTileSchema::class
    override val tileRenderer = PagerTileRenderer
    override val tileHolderBuilder = PagerTileHolderBuilder
}
