package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_rail

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationRailTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class NavigationRailTileHolder(
    override val id: String,
    override var tile: NavigationRailTileSchema,
    override val events: MutableList<EventHolder<*>>,
    private val header: TileHolder<*>?,
    private val footer: TileHolder<*>?
) : TileHolder<NavigationRailTileSchema>() {

    override val tiles: MutableList<TileHolder<*>> =
        mutableListOf<TileHolder<*>>().apply {
            header?.let(::add)
            footer?.let(::add)
        }

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() },
        header = header?.get(),
        footer = footer?.get()
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is NavigationRailTileEvents.OnItemClicked -> {
                tile = tile.copy(
                    selectedItemId = event.itemId
                )
            }
        }
    }
}
