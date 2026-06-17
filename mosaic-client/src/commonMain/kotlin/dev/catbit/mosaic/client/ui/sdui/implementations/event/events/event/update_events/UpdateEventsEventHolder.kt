package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.update_events

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class UpdateEventsEventHolder(
    override val id: String,
    override var event: UpdateEventsEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<UpdateEventsEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
