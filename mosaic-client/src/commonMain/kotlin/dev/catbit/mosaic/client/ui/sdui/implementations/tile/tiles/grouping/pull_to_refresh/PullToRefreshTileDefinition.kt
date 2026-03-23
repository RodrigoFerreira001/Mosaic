package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pull_to_refresh

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PullToRefreshTileSchema

object PullToRefreshTileDefinition : TileDefinition<PullToRefreshTileSchema> {
    override val tileSchemaClass = PullToRefreshTileSchema::class
    override val tileRenderer = PullToRefreshTileRenderer
    override val tileHolderBuilder = PullToRefreshTileHolderBuilder
}
