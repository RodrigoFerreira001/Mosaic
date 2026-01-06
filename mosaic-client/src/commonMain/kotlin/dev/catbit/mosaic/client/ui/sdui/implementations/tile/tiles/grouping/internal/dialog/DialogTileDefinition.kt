package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition

internal object DialogTileDefinition : TileDefinition<DialogTileModel, DialogTileUIState> {
    override val tileModelClass = DialogTileModel::class
    override val tileUIStateClass = DialogTileUIState::class
    override val tileRenderer = DialogTileRenderer
    override val tileUIStateProducerBuilder = DialogTileUIStateProducerBuilder
}