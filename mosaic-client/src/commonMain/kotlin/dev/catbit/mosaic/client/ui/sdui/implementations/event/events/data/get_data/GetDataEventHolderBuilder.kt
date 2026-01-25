package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.get_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.data.GetDataEventModel

object GetDataEventHolderBuilder : EventHolderBuilder<GetDataEventModel, GetDataEventHolder> {

    override fun BuilderScope.build(
        eventModel: GetDataEventModel
    ) = with(eventModel) {
        GetDataEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
