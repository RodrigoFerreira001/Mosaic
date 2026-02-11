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
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.extensions.toArrangement
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.modifiers.thenIf
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.ColumnTileSchema

// Olhar o escopo lazy e decidir se usa LazyColumn ou Apenas Column
object ColumnTileRenderer : TileRenderer<ColumnTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: ColumnTileSchema
    ) {
        with(tileSchema) {
            if (!isGone()) {
                if (lazyRender) {

                    val lazyListState = rememberLazyListState()

                    observeBroadcastChannel<ColumnTileBroadcastData> { data ->
                        if (data.tileId == id) {
                            when (data) {
                                is ColumnTileBroadcastData.ScrollToTop -> lazyListState.scrollToItem(0)
                                is ColumnTileBroadcastData.ScrollTo -> lazyListState.scrollToItem(data.index)
                                is ColumnTileBroadcastData.ScrollToBottom -> lazyListState.scrollToItem(tileSchema.tiles.size)
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .visible(isVisible())
                            .styledWith(tileSchema.style),
                        state = lazyListState,
                        verticalArrangement = arrangement.toArrangement(),
                        horizontalAlignment = alignment.toAlignment(),
                        userScrollEnabled = tileSchema.isScrollable
                    ) {
                        items(tiles) { tileSchema ->
                            RenderChild(tileSchema)
                        }
                    }
                } else {

                    val scrollState = rememberScrollState()

                    observeBroadcastChannel<ColumnTileBroadcastData> { data ->
                        if (data.tileId == tileSchema.id) {
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
                            .styledWith(style)
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
}

