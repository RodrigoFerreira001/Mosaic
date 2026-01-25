package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.overlays.navigation_drawer.DisplayNavigationDrawerEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class DisplayNavigationDrawerEventHolder(
    override val id: String,
    override var event: DisplayNavigationDrawerEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<DisplayNavigationDrawerEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
