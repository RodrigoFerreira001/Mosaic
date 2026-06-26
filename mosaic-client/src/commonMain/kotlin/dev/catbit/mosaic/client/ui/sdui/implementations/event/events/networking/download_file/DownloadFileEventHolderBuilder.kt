package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.download_file

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileEventSchema

object DownloadFileEventHolderBuilder : EventHolderBuilder<DownloadFileEventSchema, DownloadFileEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DownloadFileEventSchema
    ) = with(eventSchema) {
        DownloadFileEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
