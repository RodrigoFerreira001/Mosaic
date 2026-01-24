package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolder
import dev.catbit.mosaic.core.data.tile.tiles.buttons.ButtonTileModel

class ButtonTileHolder(
    override val id: String,
    override var tile: ButtonTileModel,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<ButtonTileModel>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}