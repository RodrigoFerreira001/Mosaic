package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationBarTileSchema

object NavigationBarTileDefinition : TileDefinition<NavigationBarTileSchema> {
    override val tileSchemaClass = NavigationBarTileSchema::class
    override val tileRenderer = NavigationBarTileRenderer
    override val tileHolderBuilder = NavigationBarTileHolderBuilder
}
