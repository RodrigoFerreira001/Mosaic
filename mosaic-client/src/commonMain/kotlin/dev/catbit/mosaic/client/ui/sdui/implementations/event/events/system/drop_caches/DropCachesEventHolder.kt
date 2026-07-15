package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.drop_caches

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.system.DropCachesEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class DropCachesEventHolder(
    override val id: String,
    override var event: DropCachesEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<DropCachesEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
