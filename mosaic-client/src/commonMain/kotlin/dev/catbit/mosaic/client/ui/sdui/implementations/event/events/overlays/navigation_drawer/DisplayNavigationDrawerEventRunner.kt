package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileBroadcastData
import dev.catbit.mosaic.core.data.event.events.overlays.navigation_drawer.DisplayNavigationDrawerEventModel

object DisplayNavigationDrawerEventRunner : EventRunner<DisplayNavigationDrawerEventModel> {
    override suspend fun EventRunningScope.runEvent(event: DisplayNavigationDrawerEventModel) {
        broadcastData(ScreenTileBroadcastData.DisplayNavigationDrawer)
    }
}