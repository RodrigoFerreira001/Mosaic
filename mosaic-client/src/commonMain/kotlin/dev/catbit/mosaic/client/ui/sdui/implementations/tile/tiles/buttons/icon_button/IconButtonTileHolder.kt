package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.icon_button

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.IconButtonTileSchema

class IconButtonTileHolder(
    override val id: String,
    override var tile: IconButtonTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<IconButtonTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}
