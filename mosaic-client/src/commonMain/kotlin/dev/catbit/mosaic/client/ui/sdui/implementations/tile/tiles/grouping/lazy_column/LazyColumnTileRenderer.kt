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
import dev.catbit.mosaic.client.extensions.observeScreenTileBroadcastChannel
import dev.catbit.mosaic.client.extensions.onClick
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.extensions.toArrangement
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalLazyItemScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column.ColumnTileScreenTilesBroadcastData
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

            observeScreenTileBroadcastChannel<ColumnTileScreenTilesBroadcastData> { data ->
                when (data) {
                    is ColumnTileScreenTilesBroadcastData.ScrollToTop -> if (data.smoothly)
                        lazyListState.animateScrollToItem(0)
                    else lazyListState.scrollToItem(0)

                    is ColumnTileScreenTilesBroadcastData.ScrollTo -> if (data.smoothly)
                        lazyListState.animateScrollToItem(data.index)
                    else lazyListState.scrollToItem(data.index)

                    is ColumnTileScreenTilesBroadcastData.ScrollToBottom -> if (data.smoothly)
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

            LazyColumn(
                modifier = modifier,
                state = lazyListState,
                verticalArrangement = arrangement.toArrangement(),
                horizontalAlignment = alignment.toAlignment(),
            ) {
                items(tiles, key = { it.id }) { tileSchema ->
                    CompositionLocalProvider(
                        LocalLazyItemScope provides this,
                        LocalColumnScope provides null
                    ) {
                        RenderChild(tileSchema)
                    }
                }
            }

            //https://slack-chats.kotlinlang.org/t/29592946/hi-everyone-i-m-working-with-compose-for-web-wasm-and-i-need

        }
    }
}
