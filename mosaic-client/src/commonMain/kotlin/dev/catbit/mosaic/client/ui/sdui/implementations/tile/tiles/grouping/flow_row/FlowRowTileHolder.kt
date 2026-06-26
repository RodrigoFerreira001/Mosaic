package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.flow_row

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlowRowTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class FlowRowTileHolder(
    override val id: String,
    override var tile: FlowRowTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<FlowRowTileSchema>() {

    override fun get() = tile.copy(
        tiles = tiles.immutableMapTo { it.get() },
        events = events.immutableMapTo { it.get() }
    )
}
