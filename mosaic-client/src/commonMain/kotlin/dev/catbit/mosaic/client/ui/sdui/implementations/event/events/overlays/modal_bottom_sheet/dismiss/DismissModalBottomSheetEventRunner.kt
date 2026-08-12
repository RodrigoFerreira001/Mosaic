package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.modal_bottom_sheet.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DismissModalBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DismissModalBottomSheetEventRunner : EventRunner<DismissModalBottomSheetEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DismissModalBottomSheetEventSchema) {
        with(event) {
            tilesOverlaysEditor.dismissModalBottomSheet(modalBottomSheetId)
                .onSuccess {
                    onTrigger(EventTriggers.onSuccess())
                }
                .onFailure {
                    onTrigger(
                        eventTrigger = EventTriggers.onFailure(),
                        data = it
                    )
                }
        }
    }
}