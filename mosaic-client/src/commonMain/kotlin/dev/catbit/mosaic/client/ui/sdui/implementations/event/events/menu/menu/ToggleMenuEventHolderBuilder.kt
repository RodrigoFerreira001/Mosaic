package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.menu.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.menu.ToggleMenuEventSchema

object ToggleMenuEventHolderBuilder : EventHolderBuilder<ToggleMenuEventSchema, ToggleMenuEventHolder> {

    override fun BuilderScope.build(
        eventSchema: ToggleMenuEventSchema
    ) = with(eventSchema) {
        ToggleMenuEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}