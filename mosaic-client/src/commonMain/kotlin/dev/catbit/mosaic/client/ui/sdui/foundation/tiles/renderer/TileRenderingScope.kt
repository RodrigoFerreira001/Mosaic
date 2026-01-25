package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTileRendererManager
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.tile.TileModel

class TileRenderingScope(
    private val tileId: String,
    private val events: List<EventModel>?,
    val onEvent: (UIEvent) -> Unit
) {
    fun dispatchEvent(tileEvent: TileEvent) {
        onEvent(UIEvent.TileEventHolderUIEvent(tileId, tileEvent))
    }

    fun triggerEvent(
        trigger: EventTrigger,
        data: Any? = null
    ) {
        events
            ?.filter { it.trigger == trigger }
            ?.let {
                onEvent(
                    UIEvent.EventModelHolderUIEvent(
                        events = it,
                        data = data
                    )
                )
            }
    }

    @Composable
    fun RenderChild(
        tileModel: TileModel,
    ) {
        with(LocalTileRendererManager.current) {
            Render(
                tileModel = tileModel,
                onEvent = onEvent
            )
        }
    }

    @Composable
    fun RenderChildren(
        tileModels: List<TileModel>,
    ) {
        with(LocalTileRendererManager.current) {
            tileModels.forEach { tileModel ->
                Render(
                    tileModel = tileModel,
                    onEvent = onEvent
                )
            }
        }
    }
}