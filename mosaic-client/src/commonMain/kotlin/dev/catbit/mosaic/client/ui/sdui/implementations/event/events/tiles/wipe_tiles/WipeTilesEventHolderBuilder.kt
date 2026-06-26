package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.wipe_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.WipeTilesEventSchema

object WipeTilesEventHolderBuilder : EventHolderBuilder<WipeTilesEventSchema, WipeTilesEventHolder> {

    override fun BuilderScope.build(
        eventSchema: WipeTilesEventSchema
    ) = with(eventSchema) {
        WipeTilesEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
