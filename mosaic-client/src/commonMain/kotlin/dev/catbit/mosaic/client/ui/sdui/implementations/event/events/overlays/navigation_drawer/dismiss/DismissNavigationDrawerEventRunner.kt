package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DismissNavigationDrawerEventSchema

object DismissNavigationDrawerEventRunner : EventRunner<DismissNavigationDrawerEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DismissNavigationDrawerEventSchema) {
        broadcastData(ScreenTileBroadcastData.DismissNavigationDrawer)
    }
}