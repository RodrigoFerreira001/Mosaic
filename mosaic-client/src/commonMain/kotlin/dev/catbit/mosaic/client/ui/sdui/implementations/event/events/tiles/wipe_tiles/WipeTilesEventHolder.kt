package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.wipe_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.WipeTilesEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class WipeTilesEventHolder(
    override val id: String,
    override var event: WipeTilesEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<WipeTilesEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
