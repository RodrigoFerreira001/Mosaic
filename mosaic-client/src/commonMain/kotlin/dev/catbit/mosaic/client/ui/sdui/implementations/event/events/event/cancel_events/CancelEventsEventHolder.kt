package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.cancel_events

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.event.CancelEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.immutableMapTo

class CancelEventsEventHolder(
    override val id: String,
    override var event: CancelEventsEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<CancelEventsEventSchema>() {

    override fun getEventSchema() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
