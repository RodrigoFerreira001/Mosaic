package dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState

class TileRendererManager(
    private val tileRenderers: List<TileRenderer<*>>
) {
    @Composable
    fun Render(
        uiState: TileUIState,
        onEvent: (UIEvent) -> Unit
    ) {
        tileRenderers.firstOrNull { it.canRender(uiState) }?.let { renderer ->
            with(renderer) {
                TileRenderingScope(
                    tileId = uiState.id,
                    onEvent = onEvent
                ).Render(uiState)
            }
        }
    }
}

val LocalTileRendererManager = compositionLocalOf<TileRendererManager> {
    error("No TileRendererManager was provided")
}