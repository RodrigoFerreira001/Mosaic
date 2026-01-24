package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.tile.tiles.menu.MenuTileModel

object MenuTileDefinition : TileDefinition<MenuTileModel> {
    override val tileModelClass = MenuTileModel::class
    override val tileRenderer = MenuTileRenderer
    override val tileHolderBuilder = MenuTileHolderBuilder
}