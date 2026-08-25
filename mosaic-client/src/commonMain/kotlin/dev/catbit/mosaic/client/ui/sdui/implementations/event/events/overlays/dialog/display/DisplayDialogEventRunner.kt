package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.display

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.events.OverlayDisplayCallbackHolder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DisplayDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DisplayDialogEventRunner : EventRunner<DisplayDialogEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DisplayDialogEventSchema) {
        with(event) {
            get<OverlayDisplayCallbackHolder>().register(dialogId) {
                onTrigger(EventTriggers.onDisplay())
            }
            tilesOverlaysEditor.addDialog(
                id = dialogId,
                isCancellable = isCancellable,
                usePlatformDefaultWidth = usePlatformDefaultWidth,
                tileSchemas = tiles
            )
                .onSuccess {
                    onTrigger(EventTriggers.onSuccess())
                }
                .onFailure {
                    get<OverlayDisplayCallbackHolder>().cancel(dialogId)
                    onTrigger(
                        eventTrigger = EventTriggers.onFailure(),
                        data = it
                    )
                }
        }
    }
}