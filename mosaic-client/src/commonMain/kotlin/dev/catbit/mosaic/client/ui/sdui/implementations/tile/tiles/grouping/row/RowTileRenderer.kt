package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.observeBroadcastChannel
import dev.catbit.mosaic.client.extensions.onClick
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.extensions.toArrangement
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.modifiers.thenIf
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalLazyRowRenderingScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.models.LazyRowRenderingScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.RowTileSchema

object RowTileRenderer : TileRenderer<RowTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: RowTileSchema
    ) {
        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(
                    style = style,
                    onClick = onClick(events)
                )

            if (lazyRender && LocalLazyRowRenderingScope.current.isUndefined()) {

                val lazyListState = rememberLazyListState()

                observeBroadcastChannel<RowTileBroadcastData> { data ->
                    when (data) {
                        is RowTileBroadcastData.ScrollToStart -> lazyListState.scrollToItem(0)
                        is RowTileBroadcastData.ScrollTo -> lazyListState.scrollToItem(data.index)
                        is RowTileBroadcastData.ScrollToEnd -> lazyListState.scrollToItem(tileSchema.tiles.size)
                    }
                }

                CompositionLocalProvider(
                    LocalLazyRowRenderingScope provides LazyRowRenderingScope.Defined
                ) {
                    LazyRow(
                        modifier = modifier,
                        state = lazyListState,
                        verticalAlignment = alignment.toAlignment(),
                        horizontalArrangement = arrangement.toArrangement(),
                        userScrollEnabled = tileSchema.isScrollable
                    ) {
                        items(tiles) { tileSchema ->
                            RenderChild(tileSchema)
                        }
                    }
                }
            } else {

                val scrollState = rememberScrollState()

                observeBroadcastChannel<RowTileBroadcastData> { data ->
                    when (data) {
                        is RowTileBroadcastData.ScrollToStart -> scrollState.scrollTo(0)
                        is RowTileBroadcastData.ScrollTo -> scrollState.scrollTo(data.index)
                        is RowTileBroadcastData.ScrollToEnd -> scrollState.scrollTo(scrollState.maxValue)
                    }
                }

                Row(
                    modifier = modifier
                        .thenIf(isScrollable) {
                            verticalScroll(scrollState)
                        },
                    verticalAlignment = alignment.toAlignment(),
                    horizontalArrangement = arrangement.toArrangement(),
                ) {
                    CompositionLocalProvider(LocalRowScope provides this) {
                        RenderChildren(tiles)
                    }
                }
            }
        }
    }
}

