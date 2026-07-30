package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.time.start_time_loop

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartTimeLoopEventSchema

object StartTimeLoopEventHolderBuilder : EventHolderBuilder<StartTimeLoopEventSchema, StartTimeLoopEventHolder> {

    override fun BuilderScope.build(
        eventSchema: StartTimeLoopEventSchema
    ) = with(eventSchema) {
        StartTimeLoopEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
