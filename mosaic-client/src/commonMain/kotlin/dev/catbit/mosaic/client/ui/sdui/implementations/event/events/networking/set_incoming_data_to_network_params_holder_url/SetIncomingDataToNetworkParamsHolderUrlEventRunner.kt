package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_url

import dev.catbit.mosaic.client.data.data_sources.network.NetworkParametersHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderUrlEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object SetIncomingDataToNetworkParamsHolderUrlEventRunner :
    EventRunner<SetIncomingDataToNetworkParamsHolderUrlEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: SetIncomingDataToNetworkParamsHolderUrlEventSchema) {
        (incomingData as? String)?.let {
            get<NetworkParametersHolder>().setUrl(it)
            onTrigger(EventTriggers.onSuccess())
        } ?: run {
            onTrigger(EventTriggers.onFailure())
            logError(
                tag = "SetIncomingDataToNetworkParamsHolderUrlEventRunner",
                throwable = Throwable("Incoming data is not a String to set as network params url")
            )
        }
    }
}
