package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.wipe_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.tiles.WipeTilesEventModel

object WipeTilesEventHolderBuilder : EventHolderBuilder<WipeTilesEventModel, WipeTilesEventHolder> {

    override fun BuilderScope.build(
        eventModel: WipeTilesEventModel
    ) = with(eventModel) {
        WipeTilesEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
