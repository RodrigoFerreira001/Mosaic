package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.remove_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.RemoveTilesEventSchema

object RemoveTilesEventHolderBuilder : EventHolderBuilder<RemoveTilesEventSchema, RemoveTilesEventHolder> {

    override fun BuilderScope.build(
        eventSchema: RemoveTilesEventSchema
    ) = with(eventSchema) {
        RemoveTilesEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
