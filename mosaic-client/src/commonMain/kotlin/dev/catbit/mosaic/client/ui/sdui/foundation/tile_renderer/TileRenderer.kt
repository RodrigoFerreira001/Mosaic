package dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState

interface TileRenderer<out T : TileUIState> {
    fun canRender(uiState: TileUIState): Boolean

    @Composable
    fun TileRenderingScope.Render(
        uiState: @UnsafeVariance T
    )
}