package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.overlays.dialog.DismissDialogEventModel

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
