package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.TextTileSchema

class TextTileHolder(
    override val id: String,
    override var tile: TextTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<TextTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}