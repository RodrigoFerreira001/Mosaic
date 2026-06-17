package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileScreenTilesBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DismissDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DismissDialogEventRunner : EventRunner<DismissDialogEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DismissDialogEventSchema) {
        broadcastData(ScreenTileScreenTilesBroadcastData.DismissDialog())
        onTrigger(EventTriggers.onSuccess())
    }
}