package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_query_parameters

import dev.catbit.mosaic.client.data.data_sources.network.NetworkParametersHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object SetIncomingDataToNetworkParamsHolderQueryParametersEventRunner :
    EventRunner<SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema> {

    @Suppress("UNCHECKED_CAST")
    override suspend fun EventRunningScope.runEvent(event: SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema) {
        (incomingData as? Map<String, Any?>)?.let {
            get<NetworkParametersHolder>().setQueryParameters(it)
            onTrigger(EventTriggers.onSuccess())
        } ?: run {
            onTrigger(EventTriggers.onFailure())
            logError(
                tag = "SetIncomingDataToNetworkParamsHolderQueryParametersEventRunner",
                throwable = Throwable("No incoming data to set as network params query parameters")
            )
        }
    }
}
