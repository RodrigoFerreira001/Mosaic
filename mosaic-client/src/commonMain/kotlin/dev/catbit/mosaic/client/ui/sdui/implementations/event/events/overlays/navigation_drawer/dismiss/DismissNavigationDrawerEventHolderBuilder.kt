package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DismissNavigationDrawerEventSchema

object DismissNavigationDrawerEventHolderBuilder : EventHolderBuilder<DismissNavigationDrawerEventSchema, DismissNavigationDrawerEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DismissNavigationDrawerEventSchema
    ) = with(eventSchema) {
        DismissNavigationDrawerEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
