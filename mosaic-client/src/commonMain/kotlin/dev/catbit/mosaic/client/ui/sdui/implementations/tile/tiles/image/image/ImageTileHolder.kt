package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.image

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.ImageTileSchema

class ImageTileHolder(
    override val id: String,
    override var tile: ImageTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<ImageTileSchema>() {
    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}
