package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.send_network_request

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.SendNetworkRequestEventModel

object SendNetworkRequestEventDefinition : EventDefinition<SendNetworkRequestEventModel> {
    override val eventModelClass = SendNetworkRequestEventModel::class
    override val eventRunner = SendNetworkRequestEventRunner
}