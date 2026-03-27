package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.progress.linear_progress

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.LinearProgressIndicatorTileSchema

object LinearProgressIndicatorTileRenderer : TileRenderer<LinearProgressIndicatorTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: LinearProgressIndicatorTileSchema,
    ) {
        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            progress?.let {
                LinearProgressIndicator(
                    modifier = modifier,
                    progress = { it }
                )
            } ?: run {
                LinearProgressIndicator(
                    modifier = modifier
                )
            }
        }
    }
}
