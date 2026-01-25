package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.display

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.overlays.navigation_drawer.DisplayNavigationDrawerEventModel

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
