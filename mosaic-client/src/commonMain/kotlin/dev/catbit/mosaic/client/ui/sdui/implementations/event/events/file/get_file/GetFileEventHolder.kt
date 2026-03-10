package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.get_file

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.file.GetFileEventSchema

class GetFileEventHolder(
    override val id: String,
    override var event: GetFileEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<GetFileEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
