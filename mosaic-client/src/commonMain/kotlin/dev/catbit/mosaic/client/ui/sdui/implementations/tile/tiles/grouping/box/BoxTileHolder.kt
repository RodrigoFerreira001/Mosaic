package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.box

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.BoxTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class BoxTileHolder(
    override val id: String,
    override var tile: BoxTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<BoxTileSchema>() {

    override fun get() = tile.copy(
        tiles = tiles.immutableMapTo { it.get() },
        events = events?.immutableMapTo { it.get() }
    )
}
