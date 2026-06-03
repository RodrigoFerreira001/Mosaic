package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.display

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DisplayNavigationDrawerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DisplayNavigationDrawerEventRunner : EventRunner<DisplayNavigationDrawerEventSchema> {
    override fun EventRunningScope.runEvent(event: DisplayNavigationDrawerEventSchema) {
        broadcastData(ScreenTileBroadcastData.DisplayNavigationDrawer())
        onTrigger(EventTriggers.onSuccess())
    }
}