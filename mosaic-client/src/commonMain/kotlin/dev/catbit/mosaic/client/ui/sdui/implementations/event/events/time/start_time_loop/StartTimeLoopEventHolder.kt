package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.time.start_time_loop

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartTimeLoopEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.immutableMapTo

class StartTimeLoopEventHolder(
    override val id: String,
    override var event: StartTimeLoopEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<StartTimeLoopEventSchema>() {

    override fun getEventSchema() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
