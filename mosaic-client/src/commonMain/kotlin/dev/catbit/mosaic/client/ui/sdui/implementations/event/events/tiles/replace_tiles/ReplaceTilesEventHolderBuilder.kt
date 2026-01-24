package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.replace_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.tiles.ReplaceTilesEventModel

object ReplaceTilesEventHolderBuilder : EventHolderBuilder<ReplaceTilesEventModel, ReplaceTilesEventHolder> {

    override fun BuilderScope.build(
        eventModel: ReplaceTilesEventModel
    ) = with(eventModel) {
        ReplaceTilesEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) },
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }
        )
    }
}
