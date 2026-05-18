package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.flex_box

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema

class FlexBoxTileHolder(
    override val id: String,
    override var tile: FlexBoxTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<FlexBoxTileSchema>() {

    override fun get() = tile.copy(
        tiles = tiles.map { it.get() },
        events = events?.map { it.get() }
    )
}
