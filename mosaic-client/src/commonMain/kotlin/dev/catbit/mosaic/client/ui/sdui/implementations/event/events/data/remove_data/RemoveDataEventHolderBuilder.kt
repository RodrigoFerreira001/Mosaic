package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema

object RemoveDataEventHolderBuilder : EventHolderBuilder<RemoveDataEventSchema, RemoveDataEventHolder> {

    override fun BuilderScope.build(
        eventSchema: RemoveDataEventSchema
    ) = with(eventSchema) {
        RemoveDataEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
