package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.download_file_to_memory

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileToMemoryEventSchema

object DownloadFileToMemoryEventDefinition : EventDefinition<DownloadFileToMemoryEventSchema> {
    override val eventSchemaClass = DownloadFileToMemoryEventSchema::class
    override val eventRunner = DownloadFileToMemoryEventRunner
    override val eventHolderBuilder = DownloadFileToMemoryEventHolderBuilder
}
