package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.get_tile_children_count

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.GetTileChildrenCountEventSchema

object GetTileChildrenCountEventHolderBuilder : EventHolderBuilder<GetTileChildrenCountEventSchema, GetTileChildrenCountEventHolder> {

    override fun BuilderScope.build(
        eventSchema: GetTileChildrenCountEventSchema
    ) = with(eventSchema) {
        GetTileChildrenCountEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
