package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.run_cancellable_events

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.event.RunCancellableEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.immutableMapTo

class RunCancellableEventsEventHolder(
    override val id: String,
    override var event: RunCancellableEventsEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<RunCancellableEventsEventSchema>() {

    override fun getEventSchema() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
