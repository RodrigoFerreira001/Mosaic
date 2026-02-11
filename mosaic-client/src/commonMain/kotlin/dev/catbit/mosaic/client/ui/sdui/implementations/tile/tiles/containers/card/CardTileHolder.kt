package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.card

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.CardTileSchema

class CardTileHolder(
    override val id: String,
    override var tile: CardTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<CardTileSchema>() {

    override fun get() = tile.copy(
        tiles = tiles.map { it.get() },
        events = events?.map { it.get() }
    )
}
