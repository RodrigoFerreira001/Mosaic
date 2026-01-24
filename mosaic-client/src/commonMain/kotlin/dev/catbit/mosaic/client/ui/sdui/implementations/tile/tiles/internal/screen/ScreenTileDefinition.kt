package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition

object ScreenTileDefinition : TileDefinition<ScreenTileModel> {
    override val tileModelClass = ScreenTileModel::class
    override val tileRenderer = ScreenTileRenderer
    override val tileHolderBuilder = ScreenTileHolderBuilder
}