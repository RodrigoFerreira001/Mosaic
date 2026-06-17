package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.input_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.InputChipTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class InputChipTileHolder(
    override val id: String,
    override var tile: InputChipTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<InputChipTileSchema>() {

    override fun get() = tile.copy(
        events = events?.immutableMapTo { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is InputChipTileEvents.OnCheckChanged -> {
                tile = tile.copy(selected = event.isSelected)
            }
        }
    }

    override fun produceValueWithKey(
        key: String
    ) = mapOf(key to tile.selected)
}
