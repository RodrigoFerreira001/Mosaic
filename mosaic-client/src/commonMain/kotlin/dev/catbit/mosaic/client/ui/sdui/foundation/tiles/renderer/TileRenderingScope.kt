package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTileRendererManager
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.collections.immutable.ImmutableList

@Stable
data class TileRenderingScope(
    val tileId: String,
    val events: ImmutableList<EventSchema>?,
    val onEvent: (UIEvent) -> Unit
) {
    fun dispatchEvent(tileEvent: TileEvent) {
        onEvent(UIEvent.TileEventHolderUIEvent(tileId, tileEvent))
    }

    fun dispatchGroupEvent(tileGroupEvent: TileGroupEvent) {
        onEvent(UIEvent.TileGroupEventHolderUIEvent(tileGroupEvent))
    }

    fun triggerEvent(
        trigger: EventTrigger,
        data: Any? = null
    ) {
        events
            ?.filter { it.trigger == trigger }
            ?.let {
                onEvent(
                    UIEvent.EventSchemaHolderUIEvent(
                        events = it,
                        data = data
                    )
                )
            }
    }

    @Composable
    fun RenderChild(
        tileSchema: TileSchema,
    ) {
        with(LocalTileRendererManager.current) {
            Render(
                tileSchema = tileSchema,
                onEvent = onEvent
            )
        }
    }

    @Composable
    fun RenderChildren(
        tileSchemas: ImmutableList<TileSchema>,
    ) {
        with(LocalTileRendererManager.current) {
            tileSchemas.forEach { tileModel ->
                Render(
                    tileSchema = tileModel,
                    onEvent = onEvent
                )
            }
        }
    }
}