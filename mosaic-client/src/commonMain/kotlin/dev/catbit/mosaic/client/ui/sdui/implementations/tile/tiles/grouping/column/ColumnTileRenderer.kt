package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.lazy.LazyColumn
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
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalLazyColumnRenderingScope
import dev.catbit.mosaic.client.ui.sdui.foundation.models.LazyColumnRenderingScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.ColumnTileSchema

object ColumnTileRenderer : TileRenderer<ColumnTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: ColumnTileSchema
    ) {
        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(
                    style = style,
                    onClick = onClick(events)
                )

            if (lazyRender && LocalLazyColumnRenderingScope.current.isUndefined()) {

                val lazyListState = rememberLazyListState()

                observeBroadcastChannel<ColumnTileBroadcastData> { data ->
                    when (data) {
                        is ColumnTileBroadcastData.ScrollToTop -> lazyListState.scrollToItem(0)
                        is ColumnTileBroadcastData.ScrollTo -> lazyListState.scrollToItem(data.index)
                        is ColumnTileBroadcastData.ScrollToBottom -> lazyListState.scrollToItem(tileSchema.tiles.size)
                    }
                }

                CompositionLocalProvider(
                    LocalLazyColumnRenderingScope provides LazyColumnRenderingScope.Defined
                ) {
                    LazyColumn(
                        modifier = modifier,
                        state = lazyListState,
                        verticalArrangement = arrangement.toArrangement(),
                        horizontalAlignment = alignment.toAlignment(),
                        userScrollEnabled = tileSchema.isScrollable
                    ) {
                        items(tiles) { tileSchema ->
                            RenderChild(tileSchema)
                        }
                    }
                }
            } else {

                val scrollState = rememberScrollState()

                observeBroadcastChannel<ColumnTileBroadcastData> { data ->
                    when (data) {
                        is ColumnTileBroadcastData.ScrollToTop -> scrollState.scrollTo(0)
                        is ColumnTileBroadcastData.ScrollTo -> scrollState.scrollTo(data.index)
                        is ColumnTileBroadcastData.ScrollToBottom -> scrollState.scrollTo(scrollState.maxValue)
                    }
                }

                Column(
                    modifier = modifier
                        .thenIf(isScrollable) {
                            verticalScroll(scrollState)
                        },
                    verticalArrangement = arrangement.toArrangement(),
                    horizontalAlignment = alignment.toAlignment(),
                ) {
                    CompositionLocalProvider(LocalColumnScope provides this) {
                        RenderChildren(tiles)
                    }
                }
            }
        }
    }
}

