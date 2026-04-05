package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.transform_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.data.TransformDataEventSchema

object TransformDataEventDefinition : EventDefinition<TransformDataEventSchema> {
    override val eventSchemaClass = TransformDataEventSchema::class
    override val eventRunner = TransformDataEventRunner
    override val eventHolderBuilder = TransformDataEventHolderBuilder
}
