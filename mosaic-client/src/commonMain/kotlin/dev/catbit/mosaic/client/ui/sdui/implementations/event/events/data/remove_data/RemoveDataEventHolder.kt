package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class RemoveDataEventHolder(
    override val id: String,
    override var event: RemoveDataEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<RemoveDataEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
