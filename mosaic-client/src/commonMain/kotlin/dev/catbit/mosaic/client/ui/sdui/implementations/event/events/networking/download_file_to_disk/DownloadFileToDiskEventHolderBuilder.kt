package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.download_file_to_disk

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileToDiskEventSchema

object DownloadFileToDiskEventHolderBuilder : EventHolderBuilder<DownloadFileToDiskEventSchema, DownloadFileToDiskEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DownloadFileToDiskEventSchema
    ) = with(eventSchema) {
        DownloadFileToDiskEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
