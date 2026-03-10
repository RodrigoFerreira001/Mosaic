package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.get_file

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.file.GetFileEventSchema

object GetFileEventDefinition : EventDefinition<GetFileEventSchema> {
    override val eventSchemaClass = GetFileEventSchema::class
    override val eventRunner = GetFileEventRunner
    override val eventHolderBuilder = GetFileEventHolderBuilder
}
