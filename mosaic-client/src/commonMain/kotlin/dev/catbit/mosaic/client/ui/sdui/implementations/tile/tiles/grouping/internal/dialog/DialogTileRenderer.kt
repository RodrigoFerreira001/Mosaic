package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderingScope

object DialogTileRenderer : TileRenderer<DialogTileUIState> {

    @Composable
    override fun TileRenderingScope.Render(
        uiState: DialogTileUIState,
    ) {
        Column {
            CompositionLocalProvider(LocalColumnScope provides this) {
                RenderChildren(uiState.tiles)
            }
        }
    }
}

