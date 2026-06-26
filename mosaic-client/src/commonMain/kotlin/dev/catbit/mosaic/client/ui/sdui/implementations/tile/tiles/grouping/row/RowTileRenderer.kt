package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.catbit.mosaic.client.extensions.ObserveScrollDirection
import dev.catbit.mosaic.client.extensions.observeScreenTileBroadcastChannel
import dev.catbit.mosaic.client.extensions.onClick
import dev.catbit.mosaic.client.extensions.toAlignment
import dev.catbit.mosaic.client.extensions.toArrangement
import dev.catbit.mosaic.client.extensions.OnDisplayEffect
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.modifiers.thenIf
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalFlowRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalLazyItemScope
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalRowScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger.ScrollDirection
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.RowTileSchema

object RowTileRenderer : TileRenderer<RowTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: RowTileSchema
    ) {

        OnDisplayEffect()

        with(tileSchema) {
            val modifier = Modifier
                .visible(isVisible())
                .styledWith(
                    style = style,
                    onClick = onClick(events)
                )

            val scrollState = rememberScrollState()

            observeScreenTileBroadcastChannel<RowTileScreenTilesBroadcastData> { data ->
                when (data) {
                    is RowTileScreenTilesBroadcastData.ScrollToStart -> if (data.smoothly)
                        scrollState.animateScrollTo(0)
                    else scrollState.scrollTo(0)

                    is RowTileScreenTilesBroadcastData.ScrollTo -> if (data.smoothly)
                        scrollState.animateScrollTo(data.index)
                    else scrollState.scrollTo(data.index)

                    is RowTileScreenTilesBroadcastData.ScrollToEnd -> if (data.smoothly)
                        scrollState.animateScrollTo(scrollState.maxValue)
                    else scrollState.scrollTo(scrollState.maxValue)
                }
            }

            scrollState.ObserveScrollDirection(
                onScrollForward = { triggerEvent(EventTriggers.onScrolled(ScrollDirection.End)) },
                onScrollBackward = { triggerEvent(EventTriggers.onScrolled(ScrollDirection.Start)) }
            )

            Row(
                modifier = modifier
                    .thenIf(scrollable) {
                        horizontalScroll(scrollState)
                    },
                verticalAlignment = alignment.toAlignment(),
                horizontalArrangement = arrangement.toArrangement(),
            ) {
                CompositionLocalProvider(
                    LocalRowScope provides this,
                    LocalLazyItemScope provides null,
                    LocalFlowRowScope provides null
                ) {
                    RenderChildren(tiles)
                }
            }
        }
    }
}
