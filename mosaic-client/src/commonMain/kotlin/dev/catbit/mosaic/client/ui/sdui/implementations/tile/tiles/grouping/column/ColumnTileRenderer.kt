package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.visible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScopeProvider
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderingScope

object ColumnTileRenderer : TileRenderer<ColumnTileUIState> {

    @Composable
    override fun TileRenderingScope.Render(
        uiState: ColumnTileUIState,
    ) {
        with(uiState) {
            if (!isGone()) {
                Column(
                    modifier = Modifier
                        .visible(isVisible())
                        .styledWith(uiState.style),
                    verticalArrangement = arrangement,
                    horizontalAlignment = alignment,
                ) {
                    CompositionLocalProvider(LocalColumnScopeProvider provides this) {
                        RenderChildren(uiState.tiles)
                    }
                }
            }
        }
    }
}

