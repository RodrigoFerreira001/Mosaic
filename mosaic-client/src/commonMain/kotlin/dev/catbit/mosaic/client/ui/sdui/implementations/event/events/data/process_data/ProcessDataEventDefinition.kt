package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.data.ProcessDataEventSchema

object ProcessDataEventDefinition : EventDefinition<ProcessDataEventSchema> {
    override val eventSchemaClass = ProcessDataEventSchema::class
    override val eventRunner = ProcessDataEventRunner
    override val eventHolderBuilder = ProcessDataEventHolderBuilder
}
