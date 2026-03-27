package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.switch

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.SwitchTileSchema

class SwitchTileHolder(
    override val id: String,
    override var tile: SwitchTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<SwitchTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when(event) {
            is SwitchTileEvents.OnCheckChanged -> {
                tile = tile.copy(checked = event.isChecked)
            }
        }
    }
}
