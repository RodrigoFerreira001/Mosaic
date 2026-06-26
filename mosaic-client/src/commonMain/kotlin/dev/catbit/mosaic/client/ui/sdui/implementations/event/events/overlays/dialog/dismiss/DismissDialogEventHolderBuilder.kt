package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DismissDialogEventSchema

object DismissDialogEventHolderBuilder : EventHolderBuilder<DismissDialogEventSchema, DismissDialogEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DismissDialogEventSchema
    ) = with(eventSchema) {
        DismissDialogEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
