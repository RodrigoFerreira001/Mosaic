package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.drop_caches

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.system.DropCachesEventSchema

object DropCachesEventHolderBuilder : EventHolderBuilder<DropCachesEventSchema, DropCachesEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DropCachesEventSchema
    ) = with(eventSchema) {
        DropCachesEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
