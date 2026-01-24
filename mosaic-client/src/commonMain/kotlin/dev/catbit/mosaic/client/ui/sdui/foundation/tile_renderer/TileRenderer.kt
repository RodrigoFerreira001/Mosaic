package dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.core.data.tile.TileModel

interface TileRenderer<out T : TileModel> {

    @Composable
    fun TileRenderingScope.Render(
        tileModel: @UnsafeVariance T
    )
}