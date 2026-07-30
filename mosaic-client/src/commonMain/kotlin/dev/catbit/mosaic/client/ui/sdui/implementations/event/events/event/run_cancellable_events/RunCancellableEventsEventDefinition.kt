package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.run_cancellable_events

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.event.RunCancellableEventsEventSchema

object RunCancellableEventsEventDefinition : EventDefinition<RunCancellableEventsEventSchema> {
    override val eventSchemaClass = RunCancellableEventsEventSchema::class
    override val eventRunner = RunCancellableEventsEventRunner
    override val eventHolderBuilder = RunCancellableEventsEventHolderBuilder
}
