package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.icon

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.IconTileSchema

class IconTileHolder(
    override val id: String,
    override var tile: IconTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<IconTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}
