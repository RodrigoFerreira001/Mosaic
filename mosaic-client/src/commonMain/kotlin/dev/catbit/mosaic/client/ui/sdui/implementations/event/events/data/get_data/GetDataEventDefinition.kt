package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.get_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.data.GetDataEventModel

object GetDataEventDefinition : EventDefinition<GetDataEventModel> {
    override val eventModelClass = GetDataEventModel::class
    override val eventRunner = GetDataEventRunner
    override val eventHolderBuilder = GetDataEventHolderBuilder
}
