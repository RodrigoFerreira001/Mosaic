package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.data.ProcessDataEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class ProcessDataEventHolder(
    override val id: String,
    override var event: ProcessDataEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<ProcessDataEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
