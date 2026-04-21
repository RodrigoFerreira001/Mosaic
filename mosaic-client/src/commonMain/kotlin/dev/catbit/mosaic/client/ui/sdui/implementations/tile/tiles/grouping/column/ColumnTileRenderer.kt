package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.ObserveScrollDirection
import dev.catbit.mosaic.client.extensions.observeBroadcastChannel
import dev.catbit.mosaic.client.extensions.onClick
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.extensions.toArrangement
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.modifiers.thenIf
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalColumnScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger.ScrollDirection
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

            val scrollState = rememberScrollState()

            observeBroadcastChannel<ColumnTileBroadcastData> { data ->
                when (data) {
                    is ColumnTileBroadcastData.ScrollToTop -> if (data.smoothly)
                        scrollState.animateScrollTo(0)
                    else scrollState.scrollTo(0)

                    is ColumnTileBroadcastData.ScrollTo -> if (data.smoothly)
                        scrollState.animateScrollTo(data.index)
                    else scrollState.scrollTo(data.index)

                    is ColumnTileBroadcastData.ScrollToBottom -> if (data.smoothly)
                        scrollState.animateScrollTo(scrollState.maxValue)
                    else scrollState.scrollTo(scrollState.maxValue)
                }
            }

            scrollState.ObserveScrollDirection(
                onScrollForward = { triggerEvent(EventTriggers.onScrolled(ScrollDirection.Bottom)) },
                onScrollBackward = { triggerEvent(EventTriggers.onScrolled(ScrollDirection.Top)) }
            )

            Column(
                modifier = modifier
                    .thenIf(scrollable) {
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
