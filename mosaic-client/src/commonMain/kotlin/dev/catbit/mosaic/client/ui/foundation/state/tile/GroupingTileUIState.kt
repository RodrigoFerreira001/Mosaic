package dev.catbit.mosaic.client.ui.foundation.state.tile

interface GroupingTileUIState : TileUIState {
    val tiles: List<TileUIState>
}