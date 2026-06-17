package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.adaptive_visibility

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema

object AdaptiveVisibilityTileDefinition : TileDefinition<AdaptiveVisibilityTileSchema> {
    override val tileSchemaClass = AdaptiveVisibilityTileSchema::class
    override val tileRenderer = AdaptiveVisibilityTileRenderer
    override val tileHolderBuilder = AdaptiveVisibilityTileHolderBuilder
}
