package dev.catbit.mosaic.client.ui.sdui.foundation.state.manager

import dev.catbit.mosaic.core.data.tile.TileModel

interface TilesOverlaysEditor {
    fun setBottomSheetTiles(tileModels: List<TileModel>)
    fun setDialogTiles(tileModels: List<TileModel>)
}