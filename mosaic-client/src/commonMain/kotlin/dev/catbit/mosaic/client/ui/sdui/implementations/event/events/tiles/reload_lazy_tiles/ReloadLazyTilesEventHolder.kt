package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.reload_lazy_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReloadLazyTilesEventSchema

class ReloadLazyTilesEventHolder(
    override val id: String,
    override var event: ReloadLazyTilesEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<ReloadLazyTilesEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
