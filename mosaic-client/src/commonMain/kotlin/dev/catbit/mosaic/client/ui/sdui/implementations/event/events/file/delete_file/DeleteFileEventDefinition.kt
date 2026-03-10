package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.delete_file

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFileEventSchema

object DeleteFileEventDefinition : EventDefinition<DeleteFileEventSchema> {
    override val eventSchemaClass = DeleteFileEventSchema::class
    override val eventRunner = DeleteFileEventRunner
    override val eventHolderBuilder = DeleteFileEventHolderBuilder
}
