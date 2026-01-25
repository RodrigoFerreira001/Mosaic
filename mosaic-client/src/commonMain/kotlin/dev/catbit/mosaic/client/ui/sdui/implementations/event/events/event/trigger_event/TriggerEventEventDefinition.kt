package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.trigger_event

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.event.TriggerEventEventModel

object TriggerEventEventDefinition : EventDefinition<TriggerEventEventModel> {
    override val eventModelClass = TriggerEventEventModel::class
    override val eventRunner = TriggerEventEventRunner
    override val eventHolderBuilder = TriggerEventEventHolderBuilder
}
