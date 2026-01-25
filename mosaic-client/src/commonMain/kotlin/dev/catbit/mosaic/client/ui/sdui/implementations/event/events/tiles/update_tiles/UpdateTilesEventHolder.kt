package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.update_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.tiles.UpdateTilesEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class UpdateTilesEventHolder(
    override val id: String,
    override var event: UpdateTilesEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<UpdateTilesEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
