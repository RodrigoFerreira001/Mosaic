package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.send_network_request

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SendNetworkRequestEventSchema

object SendNetworkRequestEventHolderBuilder : EventHolderBuilder<SendNetworkRequestEventSchema, SendNetworkRequestEventHolder> {

    override fun BuilderScope.build(
        eventSchema: SendNetworkRequestEventSchema
    ) = with(eventSchema) {
        SendNetworkRequestEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
