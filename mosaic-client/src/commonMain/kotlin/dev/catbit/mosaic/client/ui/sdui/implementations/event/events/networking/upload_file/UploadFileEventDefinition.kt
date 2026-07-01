package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.upload_file

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.networking.UploadFileEventSchema

object UploadFileEventDefinition : EventDefinition<UploadFileEventSchema> {
    override val eventSchemaClass = UploadFileEventSchema::class
    override val eventRunner = UploadFileEventRunner
    override val eventHolderBuilder = UploadFileEventHolderBuilder
}
