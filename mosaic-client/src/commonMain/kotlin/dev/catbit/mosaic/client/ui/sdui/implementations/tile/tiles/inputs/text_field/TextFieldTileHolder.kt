package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.tile.tiles.inputs.TextFieldTileModel

class TextFieldTileHolder(
    override val id: String,
    override var tile: TextFieldTileModel,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<TextFieldTileModel>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )

    override fun onTileEvent(event: TileEvent) {
        when (event) {
            is TextFieldTileEvents.OnTextChange -> {
                tile = tile.copy(value = event.newValue)
//                updateState()
            }
        }
    }
}