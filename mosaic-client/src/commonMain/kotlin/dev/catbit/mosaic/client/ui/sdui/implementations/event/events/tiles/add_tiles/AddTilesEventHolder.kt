package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class AddTilesEventHolder(
    override val id: String,
    override var event: AddTilesEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>
) : EventHolder<AddTilesEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() },
        tiles = tiles.immutableMapTo { it.get() }
    )
}
