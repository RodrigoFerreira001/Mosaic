package dev.catbit.mosaic.client.ui.state.tile

interface GroupingTileUIState : TileUIState {
    val tiles: List<TileUIState>
}