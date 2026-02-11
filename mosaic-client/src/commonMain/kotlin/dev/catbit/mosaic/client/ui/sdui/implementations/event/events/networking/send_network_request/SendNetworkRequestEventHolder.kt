package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.send_network_request

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SendNetworkRequestEventSchema

class SendNetworkRequestEventHolder(
    override val id: String,
    override var event: SendNetworkRequestEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<SendNetworkRequestEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
