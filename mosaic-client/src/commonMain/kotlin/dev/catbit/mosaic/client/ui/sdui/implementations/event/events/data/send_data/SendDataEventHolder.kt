package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.send_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.data.SendDataEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class SendDataEventHolder(
    override val id: String,
    override var event: SendDataEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<SendDataEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
