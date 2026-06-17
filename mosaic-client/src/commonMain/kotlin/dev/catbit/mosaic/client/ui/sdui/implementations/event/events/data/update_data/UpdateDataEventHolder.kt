package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.update_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.data.UpdateDataEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class UpdateDataEventHolder(
    override val id: String,
    override var event: UpdateDataEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<UpdateDataEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
