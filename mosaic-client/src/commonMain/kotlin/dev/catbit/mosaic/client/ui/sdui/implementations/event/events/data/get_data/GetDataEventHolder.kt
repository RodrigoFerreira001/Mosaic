package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.get_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.data.GetDataEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class GetDataEventHolder(
    override val id: String,
    override var event: GetDataEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<GetDataEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
