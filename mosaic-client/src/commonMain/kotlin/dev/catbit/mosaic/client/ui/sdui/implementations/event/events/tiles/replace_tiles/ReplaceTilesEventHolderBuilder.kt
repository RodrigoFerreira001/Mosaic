package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.replace_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReplaceTilesEventSchema

object ReplaceTilesEventHolderBuilder : EventHolderBuilder<ReplaceTilesEventSchema, ReplaceTilesEventHolder> {

    override fun BuilderScope.build(
        eventSchema: ReplaceTilesEventSchema
    ) = with(eventSchema) {
        ReplaceTilesEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) },
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }
        )
    }
}
