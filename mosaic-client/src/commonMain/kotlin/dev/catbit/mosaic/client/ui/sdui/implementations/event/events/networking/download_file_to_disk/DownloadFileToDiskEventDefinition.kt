package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.download_file_to_disk

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileToDiskEventSchema

object DownloadFileToDiskEventDefinition : EventDefinition<DownloadFileToDiskEventSchema> {
    override val eventSchemaClass = DownloadFileToDiskEventSchema::class
    override val eventRunner = DownloadFileToDiskEventRunner
    override val eventHolderBuilder = DownloadFileToDiskEventHolderBuilder
}
