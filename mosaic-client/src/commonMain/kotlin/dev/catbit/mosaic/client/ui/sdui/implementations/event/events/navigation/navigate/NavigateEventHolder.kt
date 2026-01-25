package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.navigation.NavigateEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class NavigateEventHolder(
    override val id: String,
    override var event: NavigateEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<NavigateEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
