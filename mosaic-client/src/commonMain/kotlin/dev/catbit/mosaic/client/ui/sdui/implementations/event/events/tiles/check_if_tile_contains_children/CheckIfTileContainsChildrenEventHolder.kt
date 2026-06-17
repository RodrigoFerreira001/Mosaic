package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.check_if_tile_contains_children

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.CheckIfTileContainsChildrenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.immutableMapTo

class CheckIfTileContainsChildrenEventHolder(
    override val id: String,
    override var event: CheckIfTileContainsChildrenEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<CheckIfTileContainsChildrenEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
