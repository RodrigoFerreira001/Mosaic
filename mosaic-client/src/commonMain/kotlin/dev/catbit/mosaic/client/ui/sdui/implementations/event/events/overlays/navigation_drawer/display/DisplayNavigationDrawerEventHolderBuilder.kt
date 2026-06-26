package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.display

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DisplayNavigationDrawerEventSchema

object DisplayNavigationDrawerEventHolderBuilder : EventHolderBuilder<DisplayNavigationDrawerEventSchema, DisplayNavigationDrawerEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DisplayNavigationDrawerEventSchema
    ) = with(eventSchema) {
        DisplayNavigationDrawerEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
