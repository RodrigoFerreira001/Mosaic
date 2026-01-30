package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.update_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.data.UpdateDataEventSchema

object UpdateDataEventHolderBuilder : EventHolderBuilder<UpdateDataEventSchema, UpdateDataEventHolder> {

    override fun BuilderScope.build(
        eventSchema: UpdateDataEventSchema
    ) = with(eventSchema) {
        UpdateDataEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
