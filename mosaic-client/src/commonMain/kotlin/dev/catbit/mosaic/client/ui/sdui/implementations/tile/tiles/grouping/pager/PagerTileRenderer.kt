package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pager

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.observeScreenTileBroadcastChannel
import dev.catbit.mosaic.client.extensions.OnDisplayEffect
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger.Direction
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PagerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PagerTileSchema.PageSizeSchema
import kotlinx.coroutines.flow.drop

object PagerTileRenderer : TileRenderer<PagerTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: PagerTileSchema) {

        OnDisplayEffect()

        with(tileSchema) {
            val pagerState = rememberPagerState(pageCount = { tiles.size })

            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }
                    .drop(1)
                    .collect { page ->
                        triggerEvent(EventTriggers.onPageChanged(Direction.Any), data = page)
                        if (page == 0) triggerEvent(EventTriggers.onPageChanged(Direction.Start), data = page)
                        if (page == pagerState.pageCount - 1) triggerEvent(EventTriggers.onPageChanged(Direction.End), data = page)
                        triggerEvent(EventTriggers.onPageChanged(Direction.Index(page)), data = page)
                    }
            }

            observeScreenTileBroadcastChannel<PagerTileScreenTilesBroadcastData> { data ->

                val lastPage = (pagerState.pageCount - 1).coerceAtLeast(0)

                val targetPage = when (data) {
                    is PagerTileScreenTilesBroadcastData.ScrollToBegin -> 0
                    is PagerTileScreenTilesBroadcastData.ScrollToEnd -> lastPage
                    is PagerTileScreenTilesBroadcastData.ScrollToNextPage -> pagerState.currentPage + 1
                    is PagerTileScreenTilesBroadcastData.ScrollToPreviousPage -> pagerState.currentPage - 1
                }.coerceIn(0, lastPage)

                if (data.smoothly) pagerState.animateScrollToPage(targetPage)
                else pagerState.scrollToPage(targetPage)
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                pageSize = pageSize.toPageSize(),
                pageSpacing = pageSpacing.dp,
                contentPadding = PaddingValues(horizontal = contentPadding.dp),
                beyondViewportPageCount = beyondViewportPageCount
            ) { page ->
                RenderChild(tiles[page])
            }
        }
    }

    private fun PageSizeSchema.toPageSize(): PageSize = when (this) {
        PageSizeSchema.Fill -> PageSize.Fill
        is PageSizeSchema.Fixed -> PageSize.Fixed(value.dp)
    }
}
