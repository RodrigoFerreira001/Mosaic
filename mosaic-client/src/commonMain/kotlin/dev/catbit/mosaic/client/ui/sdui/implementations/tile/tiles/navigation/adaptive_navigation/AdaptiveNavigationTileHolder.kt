package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.adaptive_navigation

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.AdaptiveNavigationTileSchema

class AdaptiveNavigationTileHolder(
    override val id: String,
    override var tile: AdaptiveNavigationTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    private val header: TileHolder<*>?,
    private val footer: TileHolder<*>?,
) : TileHolder<AdaptiveNavigationTileSchema>() {

    override val tiles: MutableList<TileHolder<*>> =
        mutableListOf<TileHolder<*>>().apply {
            header?.let(::add)
            footer?.let(::add)
        }

    override fun get() = tile.copy(
        events = events?.map { it.get() },
        header = header?.get(),
        footer = footer?.get(),
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is AdaptiveNavigationTileEvents.OnItemClicked -> {
                tile = tile.copy(selectedEntryId = event.entryId)
            }
        }
    }
}
