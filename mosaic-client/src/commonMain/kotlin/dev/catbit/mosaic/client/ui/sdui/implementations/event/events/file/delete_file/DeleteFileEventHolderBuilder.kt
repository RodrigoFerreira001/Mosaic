package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.delete_file

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFileEventSchema

object DeleteFileEventHolderBuilder : EventHolderBuilder<DeleteFileEventSchema, DeleteFileEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DeleteFileEventSchema
    ) = with(eventSchema) {
        DeleteFileEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
