package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.trigger_event

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.event.TriggerEventEventSchema

object TriggerEventEventHolderBuilder : EventHolderBuilder<TriggerEventEventSchema, TriggerEventEventHolder> {

    override fun BuilderScope.build(
        eventSchema: TriggerEventEventSchema
    ) = with(eventSchema) {
        TriggerEventEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
