package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.fab

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.FloatingActionButtonTileSchema

class FloatingActionButtonTileHolder(
    override val id: String,
    override var tile: FloatingActionButtonTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<FloatingActionButtonTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() },
    )
}
