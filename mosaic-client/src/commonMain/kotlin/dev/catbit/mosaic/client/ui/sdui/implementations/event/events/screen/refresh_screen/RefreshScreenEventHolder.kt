package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.refresh_screen

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.screen.RefreshScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger

class RefreshScreenEventHolder(
    override val id: String,
    override var event: RefreshScreenEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<RefreshScreenEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
