package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.modal_bottom_sheet.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DismissModalBottomSheetEventSchema

object DismissModalBottomSheetEventHolderBuilder : EventHolderBuilder<DismissModalBottomSheetEventSchema, DismissModalBottomSheetEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DismissModalBottomSheetEventSchema
    ) = with(eventSchema) {
        DismissModalBottomSheetEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
