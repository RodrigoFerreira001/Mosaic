package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.update_events

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema

object UpdateEventsEventDefinition : EventDefinition<UpdateEventsEventSchema> {
    override val eventSchemaClass = UpdateEventsEventSchema::class
    override val eventRunner = UpdateEventsEventRunner
    override val eventHolderBuilder = UpdateEventsEventHolderBuilder
}
