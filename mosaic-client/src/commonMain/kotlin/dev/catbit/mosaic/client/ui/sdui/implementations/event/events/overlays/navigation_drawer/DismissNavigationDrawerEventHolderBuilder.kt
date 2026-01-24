package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.overlays.DismissNavigationDrawerEventModel

object DismissNavigationDrawerEventHolderBuilder : EventHolderBuilder<DismissNavigationDrawerEventModel, DismissNavigationDrawerEventHolder> {

    override fun BuilderScope.build(
        eventModel: DismissNavigationDrawerEventModel
    ) = with(eventModel) {
        DismissNavigationDrawerEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
