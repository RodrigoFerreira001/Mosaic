package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderingScope

object TextTileRenderer : TileRenderer<TextTileUIState> {

    @Composable
    override fun TileRenderingScope.Render(
        uiState: TextTileUIState,
    ) {
        with(uiState) {
            if (!isGone()) {
                Text(
                    text = text,
                    modifier = Modifier
                        .visible(isVisible())
                        .styledWith(uiState.style)
                )
            }
        }
    }
}