package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.update_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.tiles.UpdateTilesEventModel

object UpdateTilesEventHolderBuilder : EventHolderBuilder<UpdateTilesEventModel, UpdateTilesEventHolder> {

    override fun BuilderScope.build(
        eventModel: UpdateTilesEventModel
    ) = with(eventModel) {
        UpdateTilesEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
