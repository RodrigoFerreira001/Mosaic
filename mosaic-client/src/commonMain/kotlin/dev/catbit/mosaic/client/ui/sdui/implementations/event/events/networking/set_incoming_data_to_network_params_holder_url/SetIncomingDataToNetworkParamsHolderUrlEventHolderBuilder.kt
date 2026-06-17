package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_url

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderUrlEventSchema

object SetIncomingDataToNetworkParamsHolderUrlEventHolderBuilder :
    EventHolderBuilder<SetIncomingDataToNetworkParamsHolderUrlEventSchema, SetIncomingDataToNetworkParamsHolderUrlEventHolder> {

    override fun BuilderScope.build(
        eventSchema: SetIncomingDataToNetworkParamsHolderUrlEventSchema
    ) = with(eventSchema) {
        SetIncomingDataToNetworkParamsHolderUrlEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
