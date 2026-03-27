package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.progress.circular_progress

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.CircularProgressIndicatorTileSchema

object CircularProgressIndicatorTileRenderer : TileRenderer<CircularProgressIndicatorTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: CircularProgressIndicatorTileSchema,
    ) {
        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            progress?.let {
                CircularProgressIndicator(
                    modifier = modifier,
                    progress = { it }
                )
            } ?: run {
                CircularProgressIndicator(
                    modifier = modifier
                )
            }
        }
    }
}
