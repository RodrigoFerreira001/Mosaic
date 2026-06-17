package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.card

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CardTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class CardTileHolder(
    override val id: String,
    override var tile: CardTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<CardTileSchema>() {

    override fun get() = tile.copy(
        tiles = tiles.immutableMapTo { it.get() },
        events = events?.immutableMapTo { it.get() }
    )
}
