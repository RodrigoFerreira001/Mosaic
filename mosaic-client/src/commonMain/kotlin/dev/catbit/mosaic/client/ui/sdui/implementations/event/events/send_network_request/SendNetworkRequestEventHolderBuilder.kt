package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.send_network_request

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.SendNetworkRequestEventModel

object SendNetworkRequestEventHolderBuilder : EventHolderBuilder<SendNetworkRequestEventModel, SendNetworkRequestEventHolder> {

    override fun BuilderScope.build(
        eventModel: SendNetworkRequestEventModel
    ) = with(eventModel) {
        SendNetworkRequestEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
