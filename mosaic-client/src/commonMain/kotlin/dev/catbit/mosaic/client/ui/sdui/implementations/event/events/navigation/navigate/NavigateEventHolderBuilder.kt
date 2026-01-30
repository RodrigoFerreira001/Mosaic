package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateEventSchema

object NavigateEventHolderBuilder : EventHolderBuilder<NavigateEventSchema, NavigateEventHolder> {

    override fun BuilderScope.build(
        eventSchema: NavigateEventSchema
    ) = with(eventSchema) {
        NavigateEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
