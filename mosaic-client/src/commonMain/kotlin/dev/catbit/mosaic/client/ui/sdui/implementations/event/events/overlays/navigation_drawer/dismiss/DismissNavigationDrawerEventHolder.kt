package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DismissNavigationDrawerEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class DismissNavigationDrawerEventHolder(
    override val id: String,
    override var event: DismissNavigationDrawerEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<DismissNavigationDrawerEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
