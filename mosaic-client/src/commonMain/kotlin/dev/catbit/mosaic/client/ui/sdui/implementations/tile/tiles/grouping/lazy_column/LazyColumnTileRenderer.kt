package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_column

import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.ObserveScrollDirection
import dev.catbit.mosaic.client.extensions.ThresholdReachedEffect
import dev.catbit.mosaic.client.extensions.observeBroadcastChannel
import dev.catbit.mosaic.client.extensions.onClick
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.extensions.toArrangement
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalLazyColumnRenderingScope
import dev.catbit.mosaic.client.ui.sdui.foundation.models.LazyColumnRenderingScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column.ColumnTileBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger.ScrollDirection
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyColumnTileSchema

object LazyColumnTileRenderer : TileRenderer<LazyColumnTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: LazyColumnTileSchema
    ) {
        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(
                    style = style,
                    onClick = onClick(events)
                )

            val lazyListState = rememberLazyListState()

            observeBroadcastChannel<ColumnTileBroadcastData> { data ->
                when (data) {
                    is ColumnTileBroadcastData.ScrollToTop -> if (data.smoothly)
                        lazyListState.animateScrollToItem(0)
                    else lazyListState.scrollToItem(0)

                    is ColumnTileBroadcastData.ScrollTo -> if (data.smoothly)
                        lazyListState.animateScrollToItem(data.index)
                    else lazyListState.scrollToItem(data.index)

                    is ColumnTileBroadcastData.ScrollToBottom -> if (data.smoothly)
                        lazyListState.animateScrollToItem(tileSchema.tiles.size)
                    else lazyListState.scrollToItem(tileSchema.tiles.size)
                }
            }

            scrollThreshold?.ThresholdReachedEffect(
                lazyListState = lazyListState,
                considerLoadingItemAtEnd = considerLoadingItemAtEndOnThresholdReached,
                onThresholdReached = {
                    triggerEvent(EventTriggers.onScrollThresholdReached())
                }
            )

            lazyListState.ObserveScrollDirection(
                onScrollForward = { triggerEvent(EventTriggers.onScrolled(ScrollDirection.Bottom)) },
                onScrollBackward = { triggerEvent(EventTriggers.onScrolled(ScrollDirection.Top)) }
            )

            CompositionLocalProvider(
                LocalLazyColumnRenderingScope provides LazyColumnRenderingScope.Defined
            ) {
                LazyColumn(
                    modifier = modifier,
                    state = lazyListState,
                    verticalArrangement = arrangement.toArrangement(),
                    horizontalAlignment = alignment.toAlignment(),
                ) {
                    items(tiles, key = { it.id }) { tileSchema ->
                        RenderChild(tileSchema)
                    }
                }
            }
        }
    }
}
