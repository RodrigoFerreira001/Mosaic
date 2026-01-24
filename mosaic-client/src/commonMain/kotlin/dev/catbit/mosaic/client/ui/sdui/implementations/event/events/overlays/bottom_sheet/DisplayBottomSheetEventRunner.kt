package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileBroadcastData
import dev.catbit.mosaic.core.data.event.events.overlays.DisplayBottomSheetEventModel

object DisplayBottomSheetEventRunner : EventRunner<DisplayBottomSheetEventModel> {
    override suspend fun EventRunningScope.runEvent(event: DisplayBottomSheetEventModel) {

        tilesOverlaysEditor.setBottomSheetTiles(event.tiles)

        broadcastData(
            ScreenTileBroadcastData.OnDisplayBottomSheetRequested(
                isCancellable = event.isCancellable,
                fill = event.fill
            )
        )
    }
}