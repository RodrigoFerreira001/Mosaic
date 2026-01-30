package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.trigger_event

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.event.TriggerEventEventSchema

object TriggerEventEventDefinition : EventDefinition<TriggerEventEventSchema> {
    override val eventSchemaClass = TriggerEventEventSchema::class
    override val eventRunner = TriggerEventEventRunner
    override val eventHolderBuilder = TriggerEventEventHolderBuilder
}
