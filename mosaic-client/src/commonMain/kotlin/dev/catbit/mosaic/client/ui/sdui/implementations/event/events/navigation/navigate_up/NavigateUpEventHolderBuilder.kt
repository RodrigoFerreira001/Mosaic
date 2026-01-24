package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.navigation.NavigateUpEventModel

object NavigateUpEventHolderBuilder : EventHolderBuilder<NavigateUpEventModel, NavigateUpEventHolder> {

    override fun BuilderScope.build(
        eventModel: NavigateUpEventModel
    ) = with(eventModel) {
        NavigateUpEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
