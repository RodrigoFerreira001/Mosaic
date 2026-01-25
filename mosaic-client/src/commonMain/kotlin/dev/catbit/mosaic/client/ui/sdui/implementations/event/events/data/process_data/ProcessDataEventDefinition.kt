package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.data.ProcessDataEventModel

object ProcessDataEventDefinition : EventDefinition<ProcessDataEventModel> {
    override val eventModelClass = ProcessDataEventModel::class
    override val eventRunner = ProcessDataEventRunner
    override val eventHolderBuilder = ProcessDataEventHolderBuilder
}
