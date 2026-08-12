package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.modal_bottom_sheet.display

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DisplayModalBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DisplayModalBottomSheetEventRunner : EventRunner<DisplayModalBottomSheetEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DisplayModalBottomSheetEventSchema) {
        with(event) {
            tilesOverlaysEditor.addModalBottomSheet(
                id = modalBottomSheetId,
                isCancellable = isCancellable,
                fill = fill,
                allowsPartialExpansion = allowsPartialExpansion,
                tileSchemas = tiles
            )
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