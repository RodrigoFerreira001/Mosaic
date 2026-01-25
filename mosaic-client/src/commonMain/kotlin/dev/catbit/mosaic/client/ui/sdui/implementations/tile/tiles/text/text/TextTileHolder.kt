package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.tile.tiles.text.TextTileModel

class TextTileHolder(
    override val id: String,
    override var tile: TextTileModel,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<TextTileModel>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}