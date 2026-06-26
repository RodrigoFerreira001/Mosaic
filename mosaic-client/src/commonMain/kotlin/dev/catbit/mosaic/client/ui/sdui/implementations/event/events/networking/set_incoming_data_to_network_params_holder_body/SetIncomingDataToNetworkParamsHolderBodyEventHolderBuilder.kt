package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_body

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderBodyEventSchema

object SetIncomingDataToNetworkParamsHolderBodyEventHolderBuilder : EventHolderBuilder<SetIncomingDataToNetworkParamsHolderBodyEventSchema, SetIncomingDataToNetworkParamsHolderBodyEventHolder> {

    override fun BuilderScope.build(
        eventSchema: SetIncomingDataToNetworkParamsHolderBodyEventSchema
    ) = with(eventSchema) {
        SetIncomingDataToNetworkParamsHolderBodyEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
