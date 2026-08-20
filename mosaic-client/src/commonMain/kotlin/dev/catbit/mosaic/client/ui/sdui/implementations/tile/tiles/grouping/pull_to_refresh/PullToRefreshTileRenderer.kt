package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pull_to_refresh

import androidx.compose.foundation.layout.visible
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.OnDisplayEffect
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPullEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PullToRefreshTileSchema

object PullToRefreshTileRenderer : TileRenderer<PullToRefreshTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: PullToRefreshTileSchema,
    ) {

        OnDisplayEffect()

        with(tileSchema) {
            PullToRefreshBox(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                isRefreshing = isRefreshing,
                onRefresh = {
                    dispatchEvent(PullToRefreshTileEvents.OnRefreshStart)
                    triggerEvent(EventTriggers.onPull())
                }
            ) {
                RenderChildren(tiles)
            }
        }
    }
}
