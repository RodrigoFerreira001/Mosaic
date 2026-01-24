package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.overlays.DismissBottomSheetEventModel

object DismissBottomSheetEventHolderBuilder : EventHolderBuilder<DismissBottomSheetEventModel, DismissBottomSheetEventHolder> {

    override fun BuilderScope.build(
        eventModel: DismissBottomSheetEventModel
    ) = with(eventModel) {
        DismissBottomSheetEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
