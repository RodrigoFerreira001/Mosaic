package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.send_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.data.SendDataEventModel

object SendDataEventHolderBuilder : EventHolderBuilder<SendDataEventModel, SendDataEventHolder> {

    override fun BuilderScope.build(
        eventModel: SendDataEventModel
    ) = with(eventModel) {
        SendDataEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
