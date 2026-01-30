package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema

object RemoveDataEventDefinition : EventDefinition<RemoveDataEventSchema> {
    override val eventSchemaClass = RemoveDataEventSchema::class
    override val eventRunner = RemoveDataEventRunner
    override val eventHolderBuilder = RemoveDataEventHolderBuilder
}
