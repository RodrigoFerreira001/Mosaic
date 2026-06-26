package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.run_events

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.event.RunEventsEventSchema

object RunEventsEventHolderBuilder : EventHolderBuilder<RunEventsEventSchema, RunEventsEventHolder> {

    override fun BuilderScope.build(
        eventSchema: RunEventsEventSchema
    ) = with(eventSchema) {
        RunEventsEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
