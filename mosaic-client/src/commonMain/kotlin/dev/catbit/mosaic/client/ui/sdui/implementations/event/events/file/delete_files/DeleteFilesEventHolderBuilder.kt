package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.delete_files

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFilesEventSchema

object DeleteFilesEventHolderBuilder : EventHolderBuilder<DeleteFilesEventSchema, DeleteFilesEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DeleteFilesEventSchema
    ) = with(eventSchema) {
        DeleteFilesEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
