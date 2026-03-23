package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.search.search_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.search.SearchBarTileSchema

class SearchBarTileHolder(
    override val id: String,
    override var tile: SearchBarTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    private val leadingIconHolder: TileHolder<*>?,
    private val trailingIconHolder: TileHolder<*>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<SearchBarTileSchema>() {

    override fun get() = tile.copy(
        events = events?.map { it.get() },
        leadingIcon = leadingIconHolder?.get(),
        trailingIcon = trailingIconHolder?.get()
    )
}
