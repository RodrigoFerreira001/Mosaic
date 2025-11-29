package dev.catbit.mosaic.client.ui.tile_renderer

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.state.tile.TileUIState

interface TileRenderer <T: TileUIState>{
    @Composable
    fun Render(uiState: T)
}