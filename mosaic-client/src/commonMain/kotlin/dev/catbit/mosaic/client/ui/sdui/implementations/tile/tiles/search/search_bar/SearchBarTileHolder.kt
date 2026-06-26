package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.search.search_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.search.SearchBarTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo
import kotlinx.collections.immutable.toImmutableList

class SearchBarTileHolder(
    override val id: String,
    override var tile: SearchBarTileSchema,
    override val events: MutableList<EventHolder<*>>,
    private val leadingIconHolder: TileHolder<*>?,
    private val trailingIconHolder: TileHolder<*>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<SearchBarTileSchema>() {

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() },
        leadingIcon = leadingIconHolder?.get(),
        trailingIcon = trailingIconHolder?.get()
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is SearchBarTileEvents.OnQueryChanged -> {
                tile = tile.copy(query = event.query)
            }

            is SearchBarTileEvents.OnQueryCleared -> {
                tile = tile.copy(query = "")
            }
        }
    }
}
