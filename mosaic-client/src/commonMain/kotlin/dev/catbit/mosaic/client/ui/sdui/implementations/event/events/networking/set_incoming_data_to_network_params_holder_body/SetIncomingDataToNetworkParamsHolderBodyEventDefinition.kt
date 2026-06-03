package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_body

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderBodyEventSchema

object SetIncomingDataToNetworkParamsHolderBodyEventDefinition : EventDefinition<SetIncomingDataToNetworkParamsHolderBodyEventSchema> {
    override val eventSchemaClass = SetIncomingDataToNetworkParamsHolderBodyEventSchema::class
    override val eventRunner = SetIncomingDataToNetworkParamsHolderBodyEventRunner
    override val eventHolderBuilder = SetIncomingDataToNetworkParamsHolderBodyEventHolderBuilder
}
