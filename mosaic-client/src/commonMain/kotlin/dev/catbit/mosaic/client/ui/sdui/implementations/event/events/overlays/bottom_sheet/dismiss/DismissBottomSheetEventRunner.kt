package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileScreenTilesBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DismissBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DismissBottomSheetEventRunner : EventRunner<DismissBottomSheetEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DismissBottomSheetEventSchema) {
        broadcastData(ScreenTileScreenTilesBroadcastData.DismissBottomSheet())
        onTrigger(EventTriggers.onSuccess())
    }
}