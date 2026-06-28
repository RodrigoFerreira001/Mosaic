package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.system.system_broadcast_listener

import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.extensions.observeSystemBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.system.SystemBroadcastListenerTileSchema

object SystemBroadcastListenerTileRenderer : TileRenderer<SystemBroadcastListenerTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: SystemBroadcastListenerTileSchema
    ) {
        with(tileSchema) {

            observeSystemBroadcastChannel(tileSchema) { data ->
                triggerEvent(
                    trigger = EventTriggers.onSystemBroadcastEventTrigger(data.broadcastId),
                    data = data.data
                )
            }

            RenderChildren(tiles)
        }
    }
}
