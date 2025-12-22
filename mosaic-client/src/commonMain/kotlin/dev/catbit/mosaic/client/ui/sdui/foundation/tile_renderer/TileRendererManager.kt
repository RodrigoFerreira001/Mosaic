package dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import kotlin.reflect.KClass

class TileRendererManager(
    private val tileRenderers: Map<KClass<out TileUIState>, TileRenderer<*>>
) {
    @Composable
    fun Render(
        uiState: TileUIState,
        onEvent: (UIEvent) -> Unit
    ) {
        tileRenderers[uiState::class]?.let { renderer ->
            with(renderer) {
                TileRenderingScope(
                    tileId = uiState.id,
                    onEvent = onEvent
                ).Render(uiState)
            }
        } ?: run {
            println("Couldn't find a renderer for $uiState") // TODO Usar logger
        }
    }
}