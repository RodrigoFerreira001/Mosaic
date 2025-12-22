package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderingScope
import dev.catbit.mosaic.core.trigger.EventTriggers

object ButtonTileRenderer : TileRenderer<ButtonTileUIState> {

    @Composable
    override fun TileRenderingScope.Render(
        uiState: ButtonTileUIState,
    ) {
        with(uiState) {
            if (!isGone()) {
                Button(
                    modifier = Modifier
                        .visible(isVisible())
                        .styledWith(uiState.style),
                    onClick = {
                        triggerEvent(EventTriggers.OnClick)
                    }
                ) {
                    Text(text)
                }
            }
        }
    }
}

