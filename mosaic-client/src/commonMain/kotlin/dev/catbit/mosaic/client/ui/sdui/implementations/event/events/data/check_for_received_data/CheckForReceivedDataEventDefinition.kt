package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.check_for_received_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.data.CheckForReceivedDataEventSchema

object CheckForReceivedDataEventDefinition : EventDefinition<CheckForReceivedDataEventSchema> {
    override val eventSchemaClass = CheckForReceivedDataEventSchema::class
    override val eventRunner = CheckForReceivedDataEventRunner
    override val eventHolderBuilder = CheckForReceivedDataEventHolderBuilder
}
