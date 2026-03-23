package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.async_image

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema

class AsyncImageTileHolder(
    override val id: String,
    override var tile: AsyncImageTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<AsyncImageTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}
