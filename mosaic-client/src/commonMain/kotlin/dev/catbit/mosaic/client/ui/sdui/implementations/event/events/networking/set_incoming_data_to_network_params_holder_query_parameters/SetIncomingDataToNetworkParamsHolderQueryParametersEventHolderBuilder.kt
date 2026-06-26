package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_query_parameters

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema

object SetIncomingDataToNetworkParamsHolderQueryParametersEventHolderBuilder :
    EventHolderBuilder<SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema, SetIncomingDataToNetworkParamsHolderQueryParametersEventHolder> {

    override fun BuilderScope.build(
        eventSchema: SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema
    ) = with(eventSchema) {
        SetIncomingDataToNetworkParamsHolderQueryParametersEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
