package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.data.ProcessDataEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class ProcessDataEventHolder(
    override val id: String,
    override var event: ProcessDataEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<ProcessDataEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
