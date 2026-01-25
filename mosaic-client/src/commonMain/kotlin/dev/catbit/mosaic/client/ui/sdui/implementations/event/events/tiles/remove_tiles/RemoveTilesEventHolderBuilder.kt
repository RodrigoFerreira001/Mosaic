package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.remove_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.tiles.RemoveTilesEventModel

object RemoveTilesEventHolderBuilder : EventHolderBuilder<RemoveTilesEventModel, RemoveTilesEventHolder> {

    override fun BuilderScope.build(
        eventModel: RemoveTilesEventModel
    ) = with(eventModel) {
        RemoveTilesEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
