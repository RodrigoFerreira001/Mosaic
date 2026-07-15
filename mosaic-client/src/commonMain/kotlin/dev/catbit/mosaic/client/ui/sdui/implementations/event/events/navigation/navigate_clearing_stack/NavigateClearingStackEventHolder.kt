package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_clearing_stack

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateClearingStackEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class NavigateClearingStackEventHolder(
    override val id: String,
    override var event: NavigateClearingStackEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<NavigateClearingStackEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
