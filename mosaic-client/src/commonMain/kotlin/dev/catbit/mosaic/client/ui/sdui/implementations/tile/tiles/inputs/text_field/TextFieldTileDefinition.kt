package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.tile.tiles.inputs.TextFieldTileModel

object TextFieldTileDefinition : TileDefinition<TextFieldTileModel, TextFieldTileUIState> {
    override val tileModelClass = TextFieldTileModel::class
    override val tileUIStateClass = TextFieldTileUIState::class
    override val tileRenderer = TextFieldRenderer
    override val tileUIStateProducerBuilder = TextFieldTileUIStateProducerBuilder
}