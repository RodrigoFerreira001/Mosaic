package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.scroll.column.ScrollColumnTileEventModel

object ScrollTileColumnEventHolderBuilder :
    EventHolderBuilder<ScrollColumnTileEventModel, ScrollTileColumnEventHolder> {

    override fun BuilderScope.build(
        eventModel: ScrollColumnTileEventModel
    ) = with(eventModel) {
        ScrollTileColumnEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
