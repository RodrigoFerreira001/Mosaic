package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.data.RemoveDataEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class RemoveDataEventHolder(
    override val id: String,
    override var event: RemoveDataEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<RemoveDataEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
