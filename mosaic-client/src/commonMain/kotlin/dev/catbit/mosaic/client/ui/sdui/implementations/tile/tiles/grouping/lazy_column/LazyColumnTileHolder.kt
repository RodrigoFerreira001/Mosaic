package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_column

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyColumnTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class LazyColumnTileHolder(
    override val id: String,
    override var tile: LazyColumnTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<LazyColumnTileSchema>() {

    override fun get() = tile.copy(
        tiles = tiles.immutableMapTo { it.get() },
        events = events.immutableMapTo { it.get() }
    )
}
