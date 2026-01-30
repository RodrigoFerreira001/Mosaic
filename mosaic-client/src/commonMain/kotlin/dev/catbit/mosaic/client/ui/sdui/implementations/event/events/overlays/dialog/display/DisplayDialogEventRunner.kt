package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.display

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DisplayDialogEventSchema

object DisplayDialogEventRunner : EventRunner<DisplayDialogEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DisplayDialogEventSchema) {

        tilesOverlaysEditor.setDialogTiles(event.tiles)

        broadcastData(
            ScreenTileBroadcastData.OnDisplayDialogRequested(
                isCancellable = event.isCancellable,
                usePlatformDefaultWidth = event.usePlatformDefaultWidth,
            )
        )
    }
}