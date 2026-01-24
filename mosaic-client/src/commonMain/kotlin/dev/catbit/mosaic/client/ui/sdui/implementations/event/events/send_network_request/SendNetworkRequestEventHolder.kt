package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.send_network_request

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.SendNetworkRequestEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class SendNetworkRequestEventHolder(
    override val id: String,
    override var event: SendNetworkRequestEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<SendNetworkRequestEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
