package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.list_files

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.file.ListFilesEventSchema

object ListFilesEventHolderBuilder : EventHolderBuilder<ListFilesEventSchema, ListFilesEventHolder> {

    override fun BuilderScope.build(
        eventSchema: ListFilesEventSchema
    ) = with(eventSchema) {
        ListFilesEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
