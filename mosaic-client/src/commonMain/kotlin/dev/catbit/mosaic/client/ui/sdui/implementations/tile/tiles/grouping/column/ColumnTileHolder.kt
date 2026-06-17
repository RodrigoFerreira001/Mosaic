package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.ColumnTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class ColumnTileHolder(
    override val id: String,
    override var tile: ColumnTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<ColumnTileSchema>() {

    override fun get() = tile.copy(
        tiles = tiles.immutableMapTo { it.get() },
        events = events?.immutableMapTo { it.get() }
    )
}