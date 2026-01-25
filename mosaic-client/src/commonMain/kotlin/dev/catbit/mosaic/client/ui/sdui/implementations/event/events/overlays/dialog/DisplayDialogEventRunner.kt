package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileBroadcastData
import dev.catbit.mosaic.core.data.event.events.overlays.dialog.DisplayDialogEventModel

object DisplayDialogEventRunner : EventRunner<DisplayDialogEventModel> {
    override suspend fun EventRunningScope.runEvent(event: DisplayDialogEventModel) {

        tilesOverlaysEditor.setDialogTiles(event.tiles)

        broadcastData(
            ScreenTileBroadcastData.OnDisplayDialogRequested(
                isCancellable = event.isCancellable,
                usePlatformDefaultWidth = event.usePlatformDefaultWidth,
            )
        )
    }
}