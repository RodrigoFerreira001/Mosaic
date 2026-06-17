package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_row

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyRowTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class LazyRowTileHolder(
    override val id: String,
    override var tile: LazyRowTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<LazyRowTileSchema>() {

    override fun get() = tile.copy(
        tiles = tiles.immutableMapTo { it.get() },
        events = events?.immutableMapTo { it.get() }
    )
}
