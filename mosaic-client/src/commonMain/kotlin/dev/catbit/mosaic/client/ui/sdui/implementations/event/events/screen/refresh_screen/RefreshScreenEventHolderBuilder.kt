package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.refresh_screen

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.screen.RefreshScreenEventSchema

object RefreshScreenEventHolderBuilder : EventHolderBuilder<RefreshScreenEventSchema, RefreshScreenEventHolder> {
    override fun BuilderScope.build(eventSchema: RefreshScreenEventSchema): RefreshScreenEventHolder =
        with(eventSchema) {
            RefreshScreenEventHolder(
                id = id,
                event = eventSchema,
                trigger = trigger,
                events = events?.map { eventModel -> buildEventHolder(eventModel) }
            )
        }
}
