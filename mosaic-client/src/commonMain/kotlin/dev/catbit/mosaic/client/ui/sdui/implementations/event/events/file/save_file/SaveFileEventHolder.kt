package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.save_file

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.file.SaveFileEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class SaveFileEventHolder(
    override val id: String,
    override var event: SaveFileEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<SaveFileEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
