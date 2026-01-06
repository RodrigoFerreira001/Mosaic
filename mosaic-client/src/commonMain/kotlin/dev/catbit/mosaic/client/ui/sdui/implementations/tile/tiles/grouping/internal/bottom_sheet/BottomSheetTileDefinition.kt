package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.bottom_sheet

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition

internal object BottomSheetTileDefinition : TileDefinition<BottomSheetTileModel, BottomSheetTileUIState> {
    override val tileModelClass = BottomSheetTileModel::class
    override val tileUIStateClass = BottomSheetTileUIState::class
    override val tileRenderer = BottomSheetTileRenderer
    override val tileUIStateProducerBuilder = BottomSheetTileUIStateProducerBuilder
}