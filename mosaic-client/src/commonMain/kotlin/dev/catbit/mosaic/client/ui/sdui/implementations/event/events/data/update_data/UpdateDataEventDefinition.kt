package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.update_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.data.UpdateDataEventSchema

object UpdateDataEventDefinition : EventDefinition<UpdateDataEventSchema> {
    override val eventSchemaClass = UpdateDataEventSchema::class
    override val eventRunner = UpdateDataEventRunner
    override val eventHolderBuilder = UpdateDataEventHolderBuilder
}
