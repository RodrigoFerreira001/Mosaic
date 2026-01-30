package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.get_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.data.GetDataEventSchema

object GetDataEventDefinition : EventDefinition<GetDataEventSchema> {
    override val eventSchemaClass = GetDataEventSchema::class
    override val eventRunner = GetDataEventRunner
    override val eventHolderBuilder = GetDataEventHolderBuilder
}
