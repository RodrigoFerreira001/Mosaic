package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.save_file

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.file.SaveFileEventSchema

object SaveFileEventHolderBuilder : EventHolderBuilder<SaveFileEventSchema, SaveFileEventHolder> {

    override fun BuilderScope.build(
        eventSchema: SaveFileEventSchema
    ) = with(eventSchema) {
        SaveFileEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
