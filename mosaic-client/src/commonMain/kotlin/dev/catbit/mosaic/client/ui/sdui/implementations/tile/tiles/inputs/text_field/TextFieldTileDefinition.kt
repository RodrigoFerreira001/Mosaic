package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.tile.tiles.inputs.TextFieldTileModel

object TextFieldTileDefinition : TileDefinition<TextFieldTileModel> {
    override val tileModelClass = TextFieldTileModel::class
    override val tileRenderer = TextFieldTileRenderer
    override val tileHolderBuilder = TextFieldTileHolderBuilder
}