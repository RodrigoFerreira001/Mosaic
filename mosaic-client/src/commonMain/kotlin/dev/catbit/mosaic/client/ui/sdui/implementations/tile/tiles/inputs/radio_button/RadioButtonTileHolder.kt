package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.radio_button

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.RadioButtonTileSchema

class RadioButtonTileHolder(
    override val id: String,
    override var tile: RadioButtonTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<RadioButtonTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() },
    )

    override fun handlesGroupEvent(event: TileGroupEvent) = event is RadioButtonTileGroupEvents

    override fun onTileGroupEvent(event: TileGroupEvent) {
        when (event) {
            is RadioButtonTileGroupEvents.OnRadioSelected -> {
                if (event.groupId == tile.groupId) {
                    tile = tile.copy(
                        selected = event.selectedTileId == tile.id
                    )
                }
            }
        }
    }
}
