package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_headers

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderHeadersEventSchema

object SetIncomingDataToNetworkParamsHolderHeadersEventDefinition : EventDefinition<SetIncomingDataToNetworkParamsHolderHeadersEventSchema> {
    override val eventSchemaClass = SetIncomingDataToNetworkParamsHolderHeadersEventSchema::class
    override val eventRunner = SetIncomingDataToNetworkParamsHolderHeadersEventRunner
    override val eventHolderBuilder = SetIncomingDataToNetworkParamsHolderHeadersEventHolderBuilder
}
