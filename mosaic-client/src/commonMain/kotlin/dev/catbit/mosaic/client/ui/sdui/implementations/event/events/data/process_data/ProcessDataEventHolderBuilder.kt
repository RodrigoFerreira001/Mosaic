package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.data.ProcessDataEventSchema

object ProcessDataEventHolderBuilder : EventHolderBuilder<ProcessDataEventSchema, ProcessDataEventHolder> {

    override fun BuilderScope.build(
        eventSchema: ProcessDataEventSchema
    ) = with(eventSchema) {
        ProcessDataEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
