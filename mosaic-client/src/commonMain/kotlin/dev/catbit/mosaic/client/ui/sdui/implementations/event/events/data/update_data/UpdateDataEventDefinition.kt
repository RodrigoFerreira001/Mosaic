package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.update_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.data.UpdateDataEventModel

object UpdateDataEventDefinition : EventDefinition<UpdateDataEventModel> {
    override val eventModelClass = UpdateDataEventModel::class
    override val eventRunner = UpdateDataEventRunner
    override val eventHolderBuilder = UpdateDataEventHolderBuilder
}
