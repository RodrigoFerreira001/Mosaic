package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.menu.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.menu.ToggleMenuEventModel

object ToggleMenuEventHolderBuilder : EventHolderBuilder<ToggleMenuEventModel, ToggleMenuEventHolder> {

    override fun BuilderScope.build(
        eventModel: ToggleMenuEventModel
    ) = with(eventModel) {
        ToggleMenuEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}