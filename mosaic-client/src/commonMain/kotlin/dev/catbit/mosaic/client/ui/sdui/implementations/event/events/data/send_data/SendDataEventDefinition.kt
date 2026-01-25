package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.send_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.data.SendDataEventModel

object SendDataEventDefinition : EventDefinition<SendDataEventModel> {
    override val eventModelClass = SendDataEventModel::class
    override val eventRunner = SendDataEventRunner
    override val eventHolderBuilder = SendDataEventHolderBuilder
}
