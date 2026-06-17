package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.filter_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.FilterChipTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class FilterChipTileHolder(
    override val id: String,
    override var tile: FilterChipTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<FilterChipTileSchema>() {

    override fun get() = tile.copy(
        events = events?.immutableMapTo { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is FilterChipTileEvents.OnCheckChanged -> {
                tile = tile.copy(selected = event.isSelected)
            }
        }
    }

    override fun produceValueWithKey(
        key: String
    ) = mapOf(key to tile.selected)
}
