package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.send_data

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.data.SendDataEventSchema

object SendDataEventDefinition : EventDefinition<SendDataEventSchema> {
    override val eventSchemaClass = SendDataEventSchema::class
    override val eventRunner = SendDataEventRunner
    override val eventHolderBuilder = SendDataEventHolderBuilder
}
