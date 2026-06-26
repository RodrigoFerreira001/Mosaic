package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.reload_lazy_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReloadLazyTilesEventSchema

object ReloadLazyTilesEventHolderBuilder : EventHolderBuilder<ReloadLazyTilesEventSchema, ReloadLazyTilesEventHolder> {

    override fun BuilderScope.build(
        eventSchema: ReloadLazyTilesEventSchema
    ) = with(eventSchema) {
        ReloadLazyTilesEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
