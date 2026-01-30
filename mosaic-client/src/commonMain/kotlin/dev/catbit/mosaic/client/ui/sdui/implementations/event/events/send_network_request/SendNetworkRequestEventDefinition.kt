package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.send_network_request

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SendNetworkRequestEventSchema

object SendNetworkRequestEventDefinition : EventDefinition<SendNetworkRequestEventSchema> {
    override val eventSchemaClass = SendNetworkRequestEventSchema::class
    override val eventRunner = SendNetworkRequestEventRunner
    override val eventHolderBuilder = SendNetworkRequestEventHolderBuilder
}