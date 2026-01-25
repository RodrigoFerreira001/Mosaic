package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.trigger_event

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.event.TriggerEventEventModel

object TriggerEventEventHolderBuilder : EventHolderBuilder<TriggerEventEventModel, TriggerEventEventHolder> {

    override fun BuilderScope.build(
        eventModel: TriggerEventEventModel
    ) = with(eventModel) {
        TriggerEventEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
