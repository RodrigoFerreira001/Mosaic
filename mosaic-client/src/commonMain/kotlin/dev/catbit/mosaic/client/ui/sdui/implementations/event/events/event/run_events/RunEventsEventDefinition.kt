package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.run_events

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.event.RunEventsEventSchema

object RunEventsEventDefinition : EventDefinition<RunEventsEventSchema> {
    override val eventSchemaClass = RunEventsEventSchema::class
    override val eventRunner = RunEventsEventRunner
    override val eventHolderBuilder = RunEventsEventHolderBuilder
}
