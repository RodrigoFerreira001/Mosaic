package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.progress.linear_progress

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.LinearProgressIndicatorTileSchema

object LinearProgressIndicatorTileRenderer : TileRenderer<LinearProgressIndicatorTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: LinearProgressIndicatorTileSchema,
    ) {
        with(tileSchema) {

        }
    }
}
