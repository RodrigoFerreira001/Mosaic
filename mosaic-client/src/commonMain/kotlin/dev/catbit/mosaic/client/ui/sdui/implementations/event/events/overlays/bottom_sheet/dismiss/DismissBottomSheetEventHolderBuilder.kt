package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DismissBottomSheetEventSchema

object DismissBottomSheetEventHolderBuilder : EventHolderBuilder<DismissBottomSheetEventSchema, DismissBottomSheetEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DismissBottomSheetEventSchema
    ) = with(eventSchema) {
        DismissBottomSheetEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
