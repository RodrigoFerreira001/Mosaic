package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.overlays.DisplayNavigationDrawerEventModel

object DisplayNavigationDrawerEventHolderBuilder : EventHolderBuilder<DisplayNavigationDrawerEventModel, DisplayNavigationDrawerEventHolder> {

    override fun BuilderScope.build(
        eventModel: DisplayNavigationDrawerEventModel
    ) = with(eventModel) {
        DisplayNavigationDrawerEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
