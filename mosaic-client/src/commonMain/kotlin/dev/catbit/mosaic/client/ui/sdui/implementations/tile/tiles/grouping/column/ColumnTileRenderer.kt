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
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.modifiers.thenIf
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScopeProvider
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRenderingScope


// TODO Separar em um plugin LazyColumn
object ColumnTileRenderer : TileRenderer<ColumnTileUIState> {

    @Composable
    override fun TileRenderingScope.Render(
        uiState: ColumnTileUIState,
    ) {
        with(uiState) {
            if (!isGone()) {
                if (uiState.lazyRender) {

                    val lazyListState = rememberLazyListState()

                    observeBroadcastChannel<ColumnTileBroadcastData> { data ->
                        if (data.tileId == uiState.id) {
                            when (data) {
                                is ColumnTileBroadcastData.ScrollToTop -> lazyListState.scrollToItem(0)
                                is ColumnTileBroadcastData.ScrollTo -> lazyListState.scrollToItem(data.index)
                                is ColumnTileBroadcastData.ScrollToBottom -> lazyListState.scrollToItem(uiState.tiles.size)
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .visible(isVisible())
                            .styledWith(uiState.style),
                        state = lazyListState,
                        verticalArrangement = arrangement,
                        horizontalAlignment = alignment,
                        userScrollEnabled = uiState.isScrollable
                    ) {
                        items(uiState.tiles) { uiState ->
                            RenderChild(uiState)
                        }
                    }
                } else {

                    val scrollState = rememberScrollState()

                    observeBroadcastChannel<ColumnTileBroadcastData> { data ->
                        if (data.tileId == uiState.id) {
                            when (data) {
                                is ColumnTileBroadcastData.ScrollToTop -> scrollState.scrollTo(0)
                                is ColumnTileBroadcastData.ScrollTo -> scrollState.scrollTo(data.index)
                                is ColumnTileBroadcastData.ScrollToBottom -> scrollState.scrollTo(scrollState.maxValue)
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .visible(isVisible())
                            .styledWith(uiState.style)
                            .thenIf(uiState.isScrollable) {
                                verticalScroll(scrollState)
                            },
                        verticalArrangement = arrangement,
                        horizontalAlignment = alignment,
                    ) {
                        CompositionLocalProvider(LocalColumnScopeProvider provides this) {
                            RenderChildren(uiState.tiles)
                        }
                    }
                }
            }
        }
    }
}

