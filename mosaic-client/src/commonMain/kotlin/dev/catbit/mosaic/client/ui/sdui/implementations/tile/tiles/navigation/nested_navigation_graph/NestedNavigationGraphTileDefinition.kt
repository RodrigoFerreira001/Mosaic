package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.nested_navigation_graph

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NestedNavigationGraphTileSchema

object NestedNavigationGraphTileDefinition : TileDefinition<NestedNavigationGraphTileSchema> {
    override val tileSchemaClass = NestedNavigationGraphTileSchema::class
    override val tileRenderer = NestedNavigationGraphTileRenderer
    override val tileHolderBuilder = NestedNavigationGraphTileHolderBuilder
}
