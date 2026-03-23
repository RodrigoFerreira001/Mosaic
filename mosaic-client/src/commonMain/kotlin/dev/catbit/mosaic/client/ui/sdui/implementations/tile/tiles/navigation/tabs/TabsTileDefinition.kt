package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.tabs

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.TabsTileSchema

object TabsTileDefinition : TileDefinition<TabsTileSchema> {
    override val tileSchemaClass = TabsTileSchema::class
    override val tileRenderer = TabsTileRenderer
    override val tileHolderBuilder = TabsTileHolderBuilder
}
