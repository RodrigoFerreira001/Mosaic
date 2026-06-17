package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.display

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileScreenTilesBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DisplayBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DisplayBottomSheetEventRunner : EventRunner<DisplayBottomSheetEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DisplayBottomSheetEventSchema) {

        tilesOverlaysEditor.setBottomSheetTiles(event.tiles)

        broadcastData(
            ScreenTileScreenTilesBroadcastData.OnDisplayBottomSheetRequested(
                isCancellable = event.isCancellable,
                fill = event.fill
            )
        )
        onTrigger(EventTriggers.onSuccess())
    }
}