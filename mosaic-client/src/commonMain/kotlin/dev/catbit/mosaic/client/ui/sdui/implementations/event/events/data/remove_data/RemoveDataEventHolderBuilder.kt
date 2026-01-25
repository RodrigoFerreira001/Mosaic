package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.data.RemoveDataEventModel

object RemoveDataEventHolderBuilder : EventHolderBuilder<RemoveDataEventModel, RemoveDataEventHolder> {

    override fun BuilderScope.build(
        eventModel: RemoveDataEventModel
    ) = with(eventModel) {
        RemoveDataEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
