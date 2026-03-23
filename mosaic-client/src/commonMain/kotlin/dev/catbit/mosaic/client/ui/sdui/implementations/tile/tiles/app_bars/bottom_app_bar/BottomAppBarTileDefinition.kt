package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.app_bars.bottom_app_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.BottomAppBarTileSchema

object BottomAppBarTileDefinition : TileDefinition<BottomAppBarTileSchema> {
    override val tileSchemaClass = BottomAppBarTileSchema::class
    override val tileRenderer = BottomAppBarTileRenderer
    override val tileHolderBuilder = BottomAppBarTileHolderBuilder
}
