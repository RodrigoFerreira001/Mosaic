package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.simple_text

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.SimpleTextTileSchema

class SimpleTextTileHolder(
    override val id: String,
    override var tile: SimpleTextTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<SimpleTextTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}
