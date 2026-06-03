package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_headers

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderHeadersEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger

class SetIncomingDataToNetworkParamsHolderHeadersEventHolder(
    override val id: String,
    override var event: SetIncomingDataToNetworkParamsHolderHeadersEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<SetIncomingDataToNetworkParamsHolderHeadersEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
