package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pull_to_refresh

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PullToRefreshTileSchema

object PullToRefreshTileRenderer : TileRenderer<PullToRefreshTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: PullToRefreshTileSchema,
    ) {
        with(tileSchema) {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    dispatchEvent(PullToRefreshTileEvents.OnRefreshStart)
                }
            ) {
                RenderChildren(tiles)
            }
        }
    }
}
