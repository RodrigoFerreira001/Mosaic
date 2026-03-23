package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.checkbox

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.CheckboxTileSchema

class CheckboxTileHolder(
    override val id: String,
    override var tile: CheckboxTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<CheckboxTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )

    override fun onTileEvent(event: TileEvent) {
        when (event) {
            is CheckboxTileEvents.OnCheckChanged -> {
                tile = tile.copy(checked = event.isChecked)
            }
        }
    }
}
