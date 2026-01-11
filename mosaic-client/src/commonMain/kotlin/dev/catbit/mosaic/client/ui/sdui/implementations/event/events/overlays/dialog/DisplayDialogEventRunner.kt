package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.overlays.DisplayDialogEventModel

object DisplayDialogEventRunner : EventRunner<DisplayDialogEventModel> {
    override suspend fun EventRunningScope.runEvent(event: DisplayDialogEventModel) {
        screenBehaviorsHolder.displayDialog(
            isCancellable = event.isCancellable,
            usePlatformDefaultWidth = event.usePlatformDefaultWidth,
            tiles = event.tiles
        )
    }
}