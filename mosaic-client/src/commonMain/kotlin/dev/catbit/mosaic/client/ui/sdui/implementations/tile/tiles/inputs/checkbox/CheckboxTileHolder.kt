package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.checkbox

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.CheckboxTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class CheckboxTileHolder(
    override val id: String,
    override var tile: CheckboxTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<CheckboxTileSchema>() {

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is CheckboxTileEvents.OnCheckChanged -> {
                tile = tile.copy(checked = event.isChecked)
            }
        }
    }

    override fun produceValueWithKey(
        key: String
    ) = mapOf(key to tile.checked)
}
