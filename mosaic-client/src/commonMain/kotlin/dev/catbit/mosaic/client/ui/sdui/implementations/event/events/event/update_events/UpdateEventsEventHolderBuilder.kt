package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.update_events

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema

object UpdateEventsEventHolderBuilder : EventHolderBuilder<UpdateEventsEventSchema, UpdateEventsEventHolder> {

    override fun BuilderScope.build(
        eventSchema: UpdateEventsEventSchema
    ) = with(eventSchema) {
        UpdateEventsEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
