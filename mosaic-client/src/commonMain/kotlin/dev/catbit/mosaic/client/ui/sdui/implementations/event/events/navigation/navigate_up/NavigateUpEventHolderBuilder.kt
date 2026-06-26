package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateUpEventSchema

object NavigateUpEventHolderBuilder : EventHolderBuilder<NavigateUpEventSchema, NavigateUpEventHolder> {

    override fun BuilderScope.build(
        eventSchema: NavigateUpEventSchema
    ) = with(eventSchema) {
        NavigateUpEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
