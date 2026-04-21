package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.adaptive_navigation

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.AdaptiveNavigationTileSchema

object AdaptiveNavigationTileDefinition : TileDefinition<AdaptiveNavigationTileSchema> {
    override val tileSchemaClass = AdaptiveNavigationTileSchema::class
    override val tileRenderer = AdaptiveNavigationTileRenderer
    override val tileHolderBuilder = AdaptiveNavigationTileHolderBuilder
}
