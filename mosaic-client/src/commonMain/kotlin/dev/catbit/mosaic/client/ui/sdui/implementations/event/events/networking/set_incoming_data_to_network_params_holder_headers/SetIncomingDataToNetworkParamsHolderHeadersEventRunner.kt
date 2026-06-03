package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_headers

import dev.catbit.mosaic.client.data.data_sources.network.NetworkParametersHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderHeadersEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object SetIncomingDataToNetworkParamsHolderHeadersEventRunner :
    EventRunner<SetIncomingDataToNetworkParamsHolderHeadersEventSchema> {

    @Suppress("UNCHECKED_CAST")
    override fun EventRunningScope.runEvent(event: SetIncomingDataToNetworkParamsHolderHeadersEventSchema) {
        incomingData?.asMapString()?.let {
            get<NetworkParametersHolder>().setHeaders(it)
            onTrigger(EventTriggers.onSuccess())
        } ?: run {
            onTrigger(EventTriggers.onFailure())
            logError(
                tag = "SetIncomingDataToNetworkParamsHolderHeadersEventRunner",
                throwable = Throwable("No incoming data to set as network params headers")
            )
        }
    }
}
