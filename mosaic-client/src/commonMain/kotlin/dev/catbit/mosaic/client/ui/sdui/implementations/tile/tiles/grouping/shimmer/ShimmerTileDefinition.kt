package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.shimmer

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.ShimmerTileSchema

object ShimmerTileDefinition : TileDefinition<ShimmerTileSchema> {
    override val tileSchemaClass = ShimmerTileSchema::class
    override val tileRenderer = ShimmerTileRenderer
    override val tileHolderBuilder = ShimmerTileHolderBuilder
}
