package dev.catbit.mosaic.sample.client.sdui.tiles.navigation.adaptive_navigation

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.sample.core.schemas.tiles.navigation.AdaptiveNavigationTileSchema

object AdaptiveNavigationTileDefinition : TileDefinition<AdaptiveNavigationTileSchema> {
    override val tileSchemaClass = AdaptiveNavigationTileSchema::class
    override val tileRenderer = AdaptiveNavigationTileRenderer
    override val tileHolderBuilder = AdaptiveNavigationTileHolderBuilder
}
