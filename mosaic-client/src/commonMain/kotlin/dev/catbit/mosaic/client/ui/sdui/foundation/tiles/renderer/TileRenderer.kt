package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

interface TileRenderer<out T : TileSchema> {

    @Composable
    fun TileRenderingScope.Render(
        tileSchema: @UnsafeVariance T
    )
}