package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_row

import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.lazy.LazyRow
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
import dev.catbit.mosaic.client.extensions.OnDisplayEffect
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalFlowRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalLazyItemScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row.RowTileScreenTilesBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger.ScrollDirection
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyRowTileSchema

object LazyRowTileRenderer : TileRenderer<LazyRowTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: LazyRowTileSchema
    ) {

        OnDisplayEffect()

        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(
                    style = style,
                    onClick = onClick(events)
                )

            val lazyListState = rememberLazyListState()

            observeScreenTileBroadcastChannel<RowTileScreenTilesBroadcastData> { data ->
                when (data) {
                    is RowTileScreenTilesBroadcastData.ScrollToStart -> if (data.smoothly)
                        lazyListState.animateScrollToItem(0)
                    else lazyListState.scrollToItem(0)

                    is RowTileScreenTilesBroadcastData.ScrollTo -> if (data.smoothly)
                        lazyListState.animateScrollToItem(data.index)
                    else lazyListState.scrollToItem(data.index)

                    is RowTileScreenTilesBroadcastData.ScrollToEnd -> if (data.smoothly)
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
                onScrollForward = { triggerEvent(EventTriggers.onScrolled(ScrollDirection.End)) },
                onScrollBackward = { triggerEvent(EventTriggers.onScrolled(ScrollDirection.Start)) }
            )

            LazyRow(
                modifier = modifier,
                state = lazyListState,
                verticalAlignment = alignment.toAlignment(),
                horizontalArrangement = arrangement.toArrangement(),
            ) {
                items(tiles, key = { it.id }) { tileSchema ->
                    CompositionLocalProvider(
                        LocalLazyItemScope provides this,
                        LocalRowScope provides null,
                        LocalFlowRowScope provides null
                    ) {
                        RenderChild(tileSchema)
                    }
                }
            }
        }
    }
}
