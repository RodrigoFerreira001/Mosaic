package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.scroll.column.ScrollTileColumnEventModel

object ScrollTileColumnEventHolderBuilder :
    EventHolderBuilder<ScrollTileColumnEventModel, ScrollTileColumnEventHolder> {

    override fun BuilderScope.build(
        eventModel: ScrollTileColumnEventModel
    ) = with(eventModel) {
        ScrollTileColumnEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
