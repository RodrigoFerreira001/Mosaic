package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DismissBottomSheetEventSchema

object DismissBottomSheetEventRunner : EventRunner<DismissBottomSheetEventSchema> {
    override fun EventRunningScope.runEvent(event: DismissBottomSheetEventSchema) {
        broadcastData(ScreenTileBroadcastData.DismissBottomSheet)
    }
}