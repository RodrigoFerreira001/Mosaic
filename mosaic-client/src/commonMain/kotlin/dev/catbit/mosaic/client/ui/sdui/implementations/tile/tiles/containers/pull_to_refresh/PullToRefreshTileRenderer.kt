package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.pull_to_refresh

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.PullToRefreshTileSchema

object PullToRefreshTileRenderer : TileRenderer<PullToRefreshTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: PullToRefreshTileSchema,
    ) {
        println("Rendering PullToRefresh")
    }
}
