package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.menu.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.menu.ToggleMenuEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class ToggleMenuEventHolder(
    override val id: String,
    override var event: ToggleMenuEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<ToggleMenuEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}