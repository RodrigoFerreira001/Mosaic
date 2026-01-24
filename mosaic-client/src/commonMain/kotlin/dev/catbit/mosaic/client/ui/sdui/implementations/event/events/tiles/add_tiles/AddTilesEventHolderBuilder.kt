package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.tiles.AddTilesEventModel

object AddTilesEventHolderBuilder : EventHolderBuilder<AddTilesEventModel, AddTilesEventHolder> {

    override fun BuilderScope.build(
        eventModel: AddTilesEventModel
    ) = with(eventModel) {
        AddTilesEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) },
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }
        )
    }
}
