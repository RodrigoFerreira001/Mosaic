package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.dropdown_list

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DropdownListTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class DropdownListTileHolder(
    override val id: String,
    override var tile: DropdownListTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<DropdownListTileSchema>() {

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is DropdownListTileEvents.OnItemSelected -> {
                tile = tile.copy(
                    selectedOptionId = event.selectedOptionId,
                    expanded = false
                )
            }

            is DropdownListTileEvents.OnDropdownListToggle -> {
                tile = tile.copy(expanded = !tile.expanded)
            }

            is DropdownListTileEvents.OnDropdownListDismissRequest -> {
                tile = tile.copy(expanded = false)
            }
        }
    }

    override fun produceValueWithKey(key: String) = mapOf(key to tile.selectedOptionId)
}
