package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DismissDialogEventSchema

object DismissDialogEventRunner : EventRunner<DismissDialogEventSchema> {
    override fun EventRunningScope.runEvent(event: DismissDialogEventSchema) {
        broadcastData(ScreenTileBroadcastData.DismissDialog)
    }
}