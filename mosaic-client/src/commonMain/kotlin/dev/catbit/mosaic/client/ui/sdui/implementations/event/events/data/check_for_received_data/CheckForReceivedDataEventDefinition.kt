package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.check_for_received_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.data.CheckForReceivedDataEventModel

object CheckForReceivedDataEventDefinition : EventDefinition<CheckForReceivedDataEventModel> {
    override val eventModelClass = CheckForReceivedDataEventModel::class
    override val eventRunner = CheckForReceivedDataEventRunner
    override val eventHolderBuilder = CheckForReceivedDataEventHolderBuilder
}
