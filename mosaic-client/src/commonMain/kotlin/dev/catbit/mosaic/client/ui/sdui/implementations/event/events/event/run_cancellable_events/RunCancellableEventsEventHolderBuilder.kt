package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.run_cancellable_events

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.event.RunCancellableEventsEventSchema

object RunCancellableEventsEventHolderBuilder :
    EventHolderBuilder<RunCancellableEventsEventSchema, RunCancellableEventsEventHolder> {

    override fun BuilderScope.build(
        eventSchema: RunCancellableEventsEventSchema
    ) = with(eventSchema) {
        RunCancellableEventsEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
