package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.replace_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReplaceTilesEventSchema

class ReplaceTilesEventHolder(
    override val id: String,
    override var event: ReplaceTilesEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>
) : EventHolder<ReplaceTilesEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() },
        tiles = tiles.map { it.get() }
    )
}
