package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.evaluate_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema

object EvaluateDataEventDefinition : EventDefinition<EvaluateDataEventSchema> {
    override val eventSchemaClass = EvaluateDataEventSchema::class
    override val eventRunner = EvaluateDataEventRunner
    override val eventHolderBuilder = EvaluateDataEventHolderBuilder
}
