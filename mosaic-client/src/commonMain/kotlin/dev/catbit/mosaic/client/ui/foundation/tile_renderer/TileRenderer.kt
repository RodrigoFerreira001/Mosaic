package dev.catbit.mosaic.client.ui.foundation.tile_renderer

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.foundation.state.tile.TileUIState

interface TileRenderer<T : TileUIState> {
    fun canRender(uiState: TileUIState)

    @Composable
    fun Render(uiState: T)
}