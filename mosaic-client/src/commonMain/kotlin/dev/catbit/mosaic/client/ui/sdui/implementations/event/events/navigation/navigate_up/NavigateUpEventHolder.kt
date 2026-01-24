package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.navigation.NavigateUpEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class NavigateUpEventHolder(
    override val id: String,
    override var event: NavigateUpEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<NavigateUpEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
