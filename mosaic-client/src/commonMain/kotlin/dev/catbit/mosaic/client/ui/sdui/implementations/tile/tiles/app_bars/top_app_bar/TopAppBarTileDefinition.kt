package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.app_bars.top_app_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.TopAppBarTileSchema

object TopAppBarTileDefinition : TileDefinition<TopAppBarTileSchema> {
    override val tileSchemaClass = TopAppBarTileSchema::class
    override val tileRenderer = TopAppBarTileRenderer
    override val tileHolderBuilder = TopAppBarTileHolderBuilder
}
