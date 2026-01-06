package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.bottom_sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderingScope

object BottomSheetTileRenderer : TileRenderer<BottomSheetTileUIState> {

    @Composable
    override fun TileRenderingScope.Render(
        uiState: BottomSheetTileUIState,
    ) {
        Column {
            CompositionLocalProvider(LocalColumnScope provides this) {
                RenderChildren(uiState.tiles)
            }
        }
    }
}

