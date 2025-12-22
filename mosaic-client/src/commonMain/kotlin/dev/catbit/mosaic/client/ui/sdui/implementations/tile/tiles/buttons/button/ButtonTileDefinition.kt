package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.tile.tiles.buttons.ButtonTileModel

object ButtonTileDefinition : TileDefinition<ButtonTileModel, ButtonTileUIState> {
    override val tileModelClass = ButtonTileModel::class
    override val tileUIStateClass = ButtonTileUIState::class
    override val tileRenderer = ButtonTileRenderer
    override val tileUIStateProducerBuilder = ButtonTileUIStateProducerBuilder
}