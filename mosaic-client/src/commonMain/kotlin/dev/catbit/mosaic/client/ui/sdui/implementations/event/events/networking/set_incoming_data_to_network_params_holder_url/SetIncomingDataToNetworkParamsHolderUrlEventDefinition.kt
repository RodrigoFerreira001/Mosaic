package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_url

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderUrlEventSchema

object SetIncomingDataToNetworkParamsHolderUrlEventDefinition :
    EventDefinition<SetIncomingDataToNetworkParamsHolderUrlEventSchema> {
    override val eventSchemaClass = SetIncomingDataToNetworkParamsHolderUrlEventSchema::class
    override val eventRunner = SetIncomingDataToNetworkParamsHolderUrlEventRunner
    override val eventHolderBuilder = SetIncomingDataToNetworkParamsHolderUrlEventHolderBuilder
}
