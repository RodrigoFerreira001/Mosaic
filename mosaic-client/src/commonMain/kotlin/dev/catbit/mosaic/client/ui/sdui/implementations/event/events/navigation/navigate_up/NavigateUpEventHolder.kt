package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateUpEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class NavigateUpEventHolder(
    override val id: String,
    override var event: NavigateUpEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<NavigateUpEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
