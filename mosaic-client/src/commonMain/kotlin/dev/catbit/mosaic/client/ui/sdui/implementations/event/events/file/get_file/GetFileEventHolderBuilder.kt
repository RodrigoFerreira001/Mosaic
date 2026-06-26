package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.get_file

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.file.GetFileEventSchema

object GetFileEventHolderBuilder : EventHolderBuilder<GetFileEventSchema, GetFileEventHolder> {

    override fun BuilderScope.build(
        eventSchema: GetFileEventSchema
    ) = with(eventSchema) {
        GetFileEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
