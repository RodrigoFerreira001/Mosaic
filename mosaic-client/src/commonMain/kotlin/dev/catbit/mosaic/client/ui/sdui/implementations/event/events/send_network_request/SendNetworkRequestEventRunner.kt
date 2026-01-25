package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.send_network_request

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event.events.networking.SendNetworkRequestEventModel
import kotlin.reflect.KClass

object SendNetworkRequestEventRunner : EventRunner<SendNetworkRequestEventModel> {

    override suspend fun EventRunningScope.runEvent(event: SendNetworkRequestEventModel) {
        print("Rodou")
    }
}