package dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.core.trigger.EventTrigger

class TileRenderingScope(
    private val tileId: String,
    private val onEvent: (UIEvent) -> Unit
) {
    fun dispatchEvent(tileEvent: TileEvent) {
        onEvent(UIEvent.TileEventHolderUIEvent(tileId, tileEvent))
    }

    fun triggerEvent(trigger: EventTrigger) {
        onEvent(UIEvent.TriggerHolderUIEvent(tileId, trigger))
    }

    @Composable
    fun RenderChild(
        uiState: TileUIState,
    ) {
        with(LocalTileRendererManager.current) {
            Render(
                uiState = uiState,
                onEvent = onEvent
            )
        }
    }

    @Composable
    fun RenderChildren(
        uiStates: List<TileUIState>,
    ) {
        with(LocalTileRendererManager.current) {
            uiStates.forEach { uiState ->
                Render(
                    uiState = uiState,
                    onEvent = onEvent
                )
            }
        }
    }
}