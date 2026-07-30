package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.cancel_events

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.event.CancelEventsEventSchema

object CancelEventsEventHolderBuilder : EventHolderBuilder<CancelEventsEventSchema, CancelEventsEventHolder> {

    override fun BuilderScope.build(
        eventSchema: CancelEventsEventSchema
    ) = with(eventSchema) {
        CancelEventsEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
