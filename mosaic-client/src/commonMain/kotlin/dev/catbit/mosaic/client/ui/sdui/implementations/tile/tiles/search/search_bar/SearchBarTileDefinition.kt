package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.search.search_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.search.SearchBarTileSchema

object SearchBarTileDefinition : TileDefinition<SearchBarTileSchema> {
    override val tileSchemaClass = SearchBarTileSchema::class
    override val tileRenderer = SearchBarTileRenderer
    override val tileHolderBuilder = SearchBarTileHolderBuilder
}
