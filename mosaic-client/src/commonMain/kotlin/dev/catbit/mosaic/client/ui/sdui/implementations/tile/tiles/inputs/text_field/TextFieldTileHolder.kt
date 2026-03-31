package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema

class TextFieldTileHolder(
    override val id: String,
    override var tile: TextFieldTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<TextFieldTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is TextFieldTileEvents.OnTextChange -> {
                tile = tile.copy(value = event.newValue)
            }
        }
    }

    override fun produceValueWithKey(
        key: String
    ) = tile.takeIf { it.value.isNotEmpty() }?.let {
        mapOf(key to it.value)
    }
}