package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.list_files

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.file.ListFilesEventSchema

object ListFilesEventDefinition : EventDefinition<ListFilesEventSchema> {
    override val eventSchemaClass = ListFilesEventSchema::class
    override val eventRunner = ListFilesEventRunner
    override val eventHolderBuilder = ListFilesEventHolderBuilder
}
