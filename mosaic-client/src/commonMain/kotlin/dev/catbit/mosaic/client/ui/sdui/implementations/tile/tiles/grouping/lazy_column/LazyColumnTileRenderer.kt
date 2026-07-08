package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_column

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.ObserveScrollDirection
import dev.catbit.mosaic.client.extensions.OnDisplayEffect
import dev.catbit.mosaic.client.extensions.ThresholdReachedEffect
import dev.catbit.mosaic.client.extensions.observeScreenTileBroadcastChannel
import dev.catbit.mosaic.client.extensions.onClick
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.extensions.toArrangement
import dev.catbit.mosaic.client.platform.Platform
import dev.catbit.mosaic.client.ui.composables.scrollbar.VerticalScrollbar
import dev.catbit.mosaic.client.ui.composables.scrollbar.defaultScrollbarStyle
import dev.catbit.mosaic.client.ui.composables.scrollbar.rememberScrollbarAdapter
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalLazyItemScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column.ColumnTileScreenTilesBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger.ScrollDirection
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyColumnTileSchema
import kotlinx.collections.immutable.toImmutableList

object LazyColumnTileRenderer : TileRenderer<LazyColumnTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: LazyColumnTileSchema
    ) {

        OnDisplayEffect()

        with(tileSchema) {
            val lazyListState = rememberLazyListState()
            val scrollbarAdapter = rememberScrollbarAdapter(lazyListState)

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

            val displayedTiles = remember(tiles, filterChildrenByTerm) {
                (filterChildrenByTerm?.takeIf { it.isNotEmpty() }?.let { filterTerm ->
                    tiles.filter { tile ->
                        tile.searchableTerms?.any {
                            it.contains(filterTerm, ignoreCase = true)
                        } == true
                    }
                } ?: tiles).toImmutableList()
            }

            Box(
                contentAlignment = Alignment.TopEnd
            ) {
                LazyColumn(
                    modifier = Modifier
                        .visible(isVisible())
                        .styledWith(
                            style = style,
                            onClick = onClick(events)
                        ),
                    state = lazyListState,
                    verticalArrangement = arrangement.toArrangement(),
                    horizontalAlignment = alignment.toAlignment(),
                ) {
                    items(displayedTiles, key = { it.id }) { tileSchema ->
                        CompositionLocalProvider(
                            LocalLazyItemScope provides this,
                            LocalColumnScope provides null
                        ) {
                            RenderChild(tileSchema)
                        }
                    }
                }
                if (Platform.name == "WasmJs" || Platform.name == "Jvm") {
                    VerticalScrollbar(
                        modifier = Modifier.fillMaxHeight(),
                        style = defaultScrollbarStyle().copy(
                            unhoverColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                            hoverColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.50f)
                        ),
                        adapter = scrollbarAdapter
                    )
                }
            }
        }
    }
}
