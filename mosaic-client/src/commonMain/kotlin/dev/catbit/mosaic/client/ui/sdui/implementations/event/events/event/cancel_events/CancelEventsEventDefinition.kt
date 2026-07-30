package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.cancel_events

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.event.CancelEventsEventSchema

object CancelEventsEventDefinition : EventDefinition<CancelEventsEventSchema> {
    override val eventSchemaClass = CancelEventsEventSchema::class
    override val eventRunner = CancelEventsEventRunner
    override val eventHolderBuilder = CancelEventsEventHolderBuilder
}
