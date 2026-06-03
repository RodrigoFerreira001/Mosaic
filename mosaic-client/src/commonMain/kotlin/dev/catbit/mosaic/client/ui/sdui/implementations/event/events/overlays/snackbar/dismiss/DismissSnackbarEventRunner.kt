package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.snackbar.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DismissSnackbarEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DismissSnackbarEventRunner : EventRunner<DismissSnackbarEventSchema> {
    override fun EventRunningScope.runEvent(event: DismissSnackbarEventSchema) {
        broadcastData(ScreenTileBroadcastData.DismissSnackbar())
        onTrigger(EventTriggers.onSuccess())
    }
}
