package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.pull_to_refresh

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.pull_to_refresh.StopRefreshingEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger

class StopRefreshingEventHolder(
    override val id: String,
    override var event: StopRefreshingEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<StopRefreshingEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
