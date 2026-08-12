package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.display

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DisplayBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object DisplayBottomSheetEventRunner : EventRunner<DisplayBottomSheetEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: DisplayBottomSheetEventSchema) {
        with(event) {
            tilesOverlaysEditor.addBottomSheet(
                id = bottomSheetId,
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
