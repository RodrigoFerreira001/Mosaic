package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.upload_file

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.networking.UploadFileEventSchema

object UploadFileEventHolderBuilder : EventHolderBuilder<UploadFileEventSchema, UploadFileEventHolder> {

    override fun BuilderScope.build(
        eventSchema: UploadFileEventSchema
    ) = with(eventSchema) {
        UploadFileEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
