package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.overlays.DismissDialogEventModel

object DismissDialogEventHolderBuilder : EventHolderBuilder<DismissDialogEventModel, DismissDialogEventHolder> {

    override fun BuilderScope.build(
        eventModel: DismissDialogEventModel
    ) = with(eventModel) {
        DismissDialogEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
