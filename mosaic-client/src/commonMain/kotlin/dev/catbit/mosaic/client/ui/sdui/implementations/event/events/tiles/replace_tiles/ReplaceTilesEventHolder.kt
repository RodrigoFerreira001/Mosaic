package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.replace_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.tiles.ReplaceTilesEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class ReplaceTilesEventHolder(
    override val id: String,
    override var event: ReplaceTilesEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>
) : EventHolder<ReplaceTilesEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() },
        tiles = tiles.map { it.get() }
    )
}
