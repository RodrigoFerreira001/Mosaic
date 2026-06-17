package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_body

import dev.catbit.mosaic.client.data.data_sources.network.NetworkParametersHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderBodyEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object SetIncomingDataToNetworkParamsHolderBodyEventRunner :
    EventRunner<SetIncomingDataToNetworkParamsHolderBodyEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: SetIncomingDataToNetworkParamsHolderBodyEventSchema) {
        incomingData?.let {
            get<NetworkParametersHolder>().setBody(it)
            onTrigger(EventTriggers.onSuccess())
        } ?: run {
            onTrigger(EventTriggers.onFailure())
            logError(
                tag = "SetIncomingDataToNetworkParamsHolderBodyEventRunner",
                throwable = Throwable("No incoming data to set as network params body")
            )
        }
    }
}
