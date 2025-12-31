package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.tile.tiles.text.TextTileModel

object TextTileDefinition : TileDefinition<TextTileModel, TextTileUIState> {
    override val tileModelClass = TextTileModel::class
    override val tileUIStateClass = TextTileUIState::class
    override val tileRenderer = TextTileRenderer
    override val tileUIStateProducerBuilder = TextTileUIStateProducerBuilder
}