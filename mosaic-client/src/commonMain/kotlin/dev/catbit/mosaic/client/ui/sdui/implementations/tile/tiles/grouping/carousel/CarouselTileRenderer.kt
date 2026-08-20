package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.carousel

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.OnDisplayEffect
import dev.catbit.mosaic.client.extensions.observeScreenTileBroadcastChannel
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pager.PagerTileScreenTilesBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger.Direction
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CarouselTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CarouselTileSchema.CarouselTypeSchema
import kotlinx.coroutines.flow.drop

object CarouselTileRenderer : TileRenderer<CarouselTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: CarouselTileSchema) {

        OnDisplayEffect()

        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(style)

            val padding = remember { PaddingValues(horizontal = contentPadding.dp) }

            val carouselState = rememberCarouselState { tiles.size }

            LaunchedEffect(carouselState) {
                snapshotFlow { carouselState.currentItem }
                    .drop(1)
                    .collect { item ->
                        triggerEvent(EventTriggers.onPageChanged(Direction.Any), data = item)
                        if (item == 0) triggerEvent(EventTriggers.onPageChanged(Direction.Start), data = item)
                        if (item == tiles.lastIndex) triggerEvent(EventTriggers.onPageChanged(Direction.End), data = item)
                        triggerEvent(EventTriggers.onPageChanged(Direction.Index(item)), data = item)
                    }
            }

            observeScreenTileBroadcastChannel<PagerTileScreenTilesBroadcastData> { data ->

                val lastItem = tiles.lastIndex.coerceAtLeast(0)

                val targetItem = when (data) {
                    is PagerTileScreenTilesBroadcastData.ScrollToBegin -> 0
                    is PagerTileScreenTilesBroadcastData.ScrollToEnd -> lastItem
                    is PagerTileScreenTilesBroadcastData.ScrollToNextPage -> carouselState.currentItem + 1
                    is PagerTileScreenTilesBroadcastData.ScrollToPreviousPage -> carouselState.currentItem - 1
                }.coerceIn(0, lastItem)

                if (data.smoothly) carouselState.animateScrollToItem(targetItem)
                else carouselState.scrollToItem(targetItem)
            }

            when (val type = type) {
                is CarouselTypeSchema.MultiBrowse -> HorizontalMultiBrowseCarousel(
                    state = carouselState,
                    modifier = modifier,
                    preferredItemWidth = type.preferredItemWidth.dp,
                    itemSpacing = itemSpacing.dp,
                    userScrollEnabled = userScrollEnabled,
                    minSmallItemWidth = type.minSmallItemWidth?.dp ?: CarouselDefaults.MinSmallItemSize,
                    maxSmallItemWidth = type.maxSmallItemWidth?.dp ?: CarouselDefaults.MaxSmallItemSize,
                    contentPadding = padding
                ) { index -> RenderChild(tiles[index]) }

                is CarouselTypeSchema.Uncontained -> HorizontalUncontainedCarousel(
                    state = carouselState,
                    modifier = modifier,
                    itemWidth = type.itemWidth.dp,
                    itemSpacing = itemSpacing.dp,
                    userScrollEnabled = userScrollEnabled,
                    contentPadding = padding
                ) { index -> RenderChild(tiles[index]) }
            }
        }
    }
}
