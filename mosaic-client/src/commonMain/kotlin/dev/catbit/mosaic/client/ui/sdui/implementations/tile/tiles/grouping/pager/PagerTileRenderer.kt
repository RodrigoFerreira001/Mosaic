package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pager

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.observeBroadcastChannel
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalSharedHorizontalArea
import dev.catbit.mosaic.client.ui.sdui.foundation.models.SharedHorizontalArea
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PagerTileSchema

object PagerTileRenderer : TileRenderer<PagerTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: PagerTileSchema,
    ) {
        with(tileSchema) {

            val pagerState = rememberPagerState(pageCount = { tiles.size })

            observeBroadcastChannel<PagerTileBroadcastData> { data ->
                when (data) {
                    is PagerTileBroadcastData.ScrollToBegin -> pagerState.scrollToPage(0)
                    is PagerTileBroadcastData.ScrollToEnd -> pagerState.scrollToPage(pagerState.pageCount)
                    is PagerTileBroadcastData.ScrollToNextPage -> pagerState.scrollToPage(pagerState.currentPage + 1)
                    is PagerTileBroadcastData.ScrollToPreviousPage -> pagerState.scrollToPage(pagerState.currentPage - 1)
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style)
            ) {
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = gutter.dp,
                    contentPadding = PaddingValues(horizontal = contentHorizontalPadding.dp),
                    beyondViewportPageCount = 1
                ) { page ->
                    CompositionLocalProvider(
                        LocalSharedHorizontalArea provides SharedHorizontalArea.Defined(
                            columns = columns,
                            gutter = gutter,
                            totalWidth = this@BoxWithConstraints.maxWidth.value,
                            horizontalPadding = contentHorizontalPadding
                        )
                    ) {
                        RenderChild(tiles[page])
                    }
                }
            }
        }
    }
}
