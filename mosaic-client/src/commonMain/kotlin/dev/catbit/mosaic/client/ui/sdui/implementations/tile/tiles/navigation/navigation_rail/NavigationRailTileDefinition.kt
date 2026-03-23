package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_rail

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationRailTileSchema

object NavigationRailTileDefinition : TileDefinition<NavigationRailTileSchema> {
    override val tileSchemaClass = NavigationRailTileSchema::class
    override val tileRenderer = NavigationRailTileRenderer
    override val tileHolderBuilder = NavigationRailTileHolderBuilder
}
