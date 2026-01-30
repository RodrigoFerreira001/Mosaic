package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema

class ScrollTileColumnEventHolder(
    override val id: String,
    override var event: ScrollColumnTileEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<ScrollColumnTileEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
