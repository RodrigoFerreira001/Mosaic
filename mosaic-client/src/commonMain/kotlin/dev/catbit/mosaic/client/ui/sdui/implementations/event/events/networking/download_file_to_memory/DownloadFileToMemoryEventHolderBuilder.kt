package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.download_file_to_memory

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileToMemoryEventSchema

object DownloadFileToMemoryEventHolderBuilder : EventHolderBuilder<DownloadFileToMemoryEventSchema, DownloadFileToMemoryEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DownloadFileToMemoryEventSchema
    ) = with(eventSchema) {
        DownloadFileToMemoryEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
