package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.check_if_tile_contains_children

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.CheckIfTileContainsChildrenEventSchema

object CheckIfTileContainsChildrenEventHolderBuilder : EventHolderBuilder<CheckIfTileContainsChildrenEventSchema, CheckIfTileContainsChildrenEventHolder> {

    override fun BuilderScope.build(
        eventSchema: CheckIfTileContainsChildrenEventSchema
    ) = with(eventSchema) {
        CheckIfTileContainsChildrenEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
