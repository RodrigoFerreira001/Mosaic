package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.scroll.column.ScrollTileColumnEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class ScrollTileColumnEventHolder(
    override val id: String,
    override var event: ScrollTileColumnEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<ScrollTileColumnEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
