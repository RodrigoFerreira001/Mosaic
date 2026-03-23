package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.pager

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager.ScrollPagerTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger

class ScrollTilePagerEventHolder(
    override val id: String,
    override var event: ScrollPagerTileEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<ScrollPagerTileEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
