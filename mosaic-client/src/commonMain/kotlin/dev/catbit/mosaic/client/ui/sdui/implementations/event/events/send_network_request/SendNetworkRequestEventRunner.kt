package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.send_network_request

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event.events.SendNetworkRequestEventModel

class SendNetworkRequestEventRunner : EventRunner<SendNetworkRequestEventModel> {

    override fun canRun(event: EventModel) = event is SendNetworkRequestEventModel

    override fun EventRunningScope.runEvent(event: SendNetworkRequestEventModel) {
        print("Rodou")
    }
}