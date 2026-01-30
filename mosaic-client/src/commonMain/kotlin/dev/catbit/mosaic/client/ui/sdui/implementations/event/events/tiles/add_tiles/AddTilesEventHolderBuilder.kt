package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema

object AddTilesEventHolderBuilder : EventHolderBuilder<AddTilesEventSchema, AddTilesEventHolder> {

    override fun BuilderScope.build(
        eventSchema: AddTilesEventSchema
    ) = with(eventSchema) {
        AddTilesEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) },
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }
        )
    }
}
