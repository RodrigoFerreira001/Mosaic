package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.delete_files

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFilesEventSchema

object DeleteFilesEventDefinition : EventDefinition<DeleteFilesEventSchema> {
    override val eventSchemaClass = DeleteFilesEventSchema::class
    override val eventRunner = DeleteFilesEventRunner
    override val eventHolderBuilder = DeleteFilesEventHolderBuilder
}
