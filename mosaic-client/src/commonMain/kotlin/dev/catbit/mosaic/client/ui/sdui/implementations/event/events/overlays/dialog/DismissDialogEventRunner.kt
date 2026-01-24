package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileBroadcastData
import dev.catbit.mosaic.core.data.event.events.overlays.DismissDialogEventModel

object DismissDialogEventRunner : EventRunner<DismissDialogEventModel> {
    override suspend fun EventRunningScope.runEvent(event: DismissDialogEventModel) {
        broadcastData(ScreenTileBroadcastData.DismissDialog)
    }
}