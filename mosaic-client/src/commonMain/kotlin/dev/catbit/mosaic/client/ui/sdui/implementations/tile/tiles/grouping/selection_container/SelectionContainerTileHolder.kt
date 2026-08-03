package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.selection_container

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.SelectionContainerTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class SelectionContainerTileHolder(
    override val id: String,
    override var tile: SelectionContainerTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<SelectionContainerTileSchema>() {

    override fun getTileSchema() = tile.copy(
        tiles = tiles.immutableMapTo { it.get() },
        events = events.immutableMapTo { it.get() }
    )
}
