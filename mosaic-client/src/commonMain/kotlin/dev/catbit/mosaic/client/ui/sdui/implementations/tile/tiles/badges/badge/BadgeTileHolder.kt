package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.badges.badge

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.badges.BadgeTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class BadgeTileHolder(
    override val id: String,
    override var tile: BadgeTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<BadgeTileSchema>() {

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() }
    )
}
