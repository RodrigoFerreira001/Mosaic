package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.save_file

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.file.SaveFileEventSchema

object SaveFileEventDefinition : EventDefinition<SaveFileEventSchema> {
    override val eventSchemaClass = SaveFileEventSchema::class
    override val eventRunner = SaveFileEventRunner
    override val eventHolderBuilder = SaveFileEventHolderBuilder
}
