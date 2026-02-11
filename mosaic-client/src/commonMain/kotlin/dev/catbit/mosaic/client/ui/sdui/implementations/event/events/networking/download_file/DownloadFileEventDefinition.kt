package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.download_file

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileEventSchema

object DownloadFileEventDefinition : EventDefinition<DownloadFileEventSchema> {
    override val eventSchemaClass = DownloadFileEventSchema::class
    override val eventRunner = DownloadFileEventRunner
    override val eventHolderBuilder = DownloadFileEventHolderBuilder
}
