package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.data.RemoveDataEventModel

object RemoveDataEventDefinition : EventDefinition<RemoveDataEventModel> {
    override val eventModelClass = RemoveDataEventModel::class
    override val eventRunner = RemoveDataEventRunner
    override val eventHolderBuilder = RemoveDataEventHolderBuilder
}
