package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.send_network_request

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SendNetworkRequestEventSchema

object SendNetworkRequestEventRunner : EventRunner<SendNetworkRequestEventSchema> {

    override fun EventRunningScope.runEvent(event: SendNetworkRequestEventSchema) {
        print("Rodou")
    }
}