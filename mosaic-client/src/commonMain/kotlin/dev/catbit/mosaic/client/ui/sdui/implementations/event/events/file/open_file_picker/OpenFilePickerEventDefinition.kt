package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.open_file_picker

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.file.OpenFilePickerEventSchema

object OpenFilePickerEventDefinition : EventDefinition<OpenFilePickerEventSchema> {
    override val eventSchemaClass = OpenFilePickerEventSchema::class
    override val eventRunner = OpenFilePickerEventRunner
    override val eventHolderBuilder = OpenFilePickerEventHolderBuilder
}
