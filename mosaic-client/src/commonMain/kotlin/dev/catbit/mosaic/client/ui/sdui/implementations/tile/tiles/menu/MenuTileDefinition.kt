package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.menu.MenuTileSchema

object MenuTileDefinition : TileDefinition<MenuTileSchema> {
    override val tileSchemaClass = MenuTileSchema::class
    override val tileRenderer = MenuTileRenderer
    override val tileHolderBuilder = MenuTileHolderBuilder
}