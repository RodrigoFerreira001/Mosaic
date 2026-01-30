package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema

class ButtonTileHolder(
    override val id: String,
    override var tile: ButtonTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<ButtonTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )
}