package dev.catbit.mosaic.client.ui.sdui.foundation.state.tile

interface GroupingTileUIState : TileUIState {
    val tiles: List<TileUIState>
}