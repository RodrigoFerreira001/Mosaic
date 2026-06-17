package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.send_file

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SendFileEventSchema

object SendFileEventDefinition : EventDefinition<SendFileEventSchema> {
    override val eventSchemaClass = SendFileEventSchema::class
    override val eventRunner = SendFileEventRunner
    override val eventHolderBuilder = SendFileEventHolderBuilder
}
