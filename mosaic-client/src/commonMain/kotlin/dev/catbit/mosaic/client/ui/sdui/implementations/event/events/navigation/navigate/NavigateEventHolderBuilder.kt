package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.navigation.NavigateEventModel

object NavigateEventHolderBuilder : EventHolderBuilder<NavigateEventModel, NavigateEventHolder> {

    override fun BuilderScope.build(
        eventModel: NavigateEventModel
    ) = with(eventModel) {
        NavigateEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
