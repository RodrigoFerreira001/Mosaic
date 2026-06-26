package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.pull_to_refresh

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.pull_to_refresh.StopRefreshingEventSchema

object StopRefreshingEventHolderBuilder :
    EventHolderBuilder<StopRefreshingEventSchema, StopRefreshingEventHolder> {

    override fun BuilderScope.build(
        eventSchema: StopRefreshingEventSchema
    ) = with(eventSchema) {
        StopRefreshingEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
