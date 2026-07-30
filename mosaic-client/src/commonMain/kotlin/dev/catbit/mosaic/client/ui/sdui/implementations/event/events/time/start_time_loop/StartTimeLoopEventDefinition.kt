package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.time.start_time_loop

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartTimeLoopEventSchema

object StartTimeLoopEventDefinition : EventDefinition<StartTimeLoopEventSchema> {
    override val eventSchemaClass = StartTimeLoopEventSchema::class
    override val eventRunner = StartTimeLoopEventRunner
    override val eventHolderBuilder = StartTimeLoopEventHolderBuilder
}
