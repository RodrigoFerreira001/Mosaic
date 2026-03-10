package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.delete_file

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFileEventSchema

class DeleteFileEventHolder(
    override val id: String,
    override var event: DeleteFileEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<DeleteFileEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
