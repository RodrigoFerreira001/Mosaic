package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.update_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.data.UpdateDataEventModel

object UpdateDataEventHolderBuilder : EventHolderBuilder<UpdateDataEventModel, UpdateDataEventHolder> {

    override fun BuilderScope.build(
        eventModel: UpdateDataEventModel
    ) = with(eventModel) {
        UpdateDataEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
