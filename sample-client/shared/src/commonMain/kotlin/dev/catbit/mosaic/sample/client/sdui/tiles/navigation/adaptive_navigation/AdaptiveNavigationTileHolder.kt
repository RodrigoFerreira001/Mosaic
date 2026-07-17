package dev.catbit.mosaic.sample.client.sdui.tiles.navigation.adaptive_navigation

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.extensions.immutableMapTo
import dev.catbit.mosaic.sample.core.schemas.tiles.navigation.AdaptiveNavigationTileSchema

class AdaptiveNavigationTileHolder(
    override val id: String,
    override var tile: AdaptiveNavigationTileSchema,
    override val events: MutableList<EventHolder<*>>,
    private val primaryAction: TileHolder<*>?,
    private val topBarTitle: TileHolder<*>,
    private val topBarSubtitle: MutableList<TileHolder<*>>?,
    private val topBarActions: MutableList<TileHolder<*>>?,
) : TileHolder<AdaptiveNavigationTileSchema>() {

    override val tiles: MutableList<TileHolder<*>> =
        mutableListOf<TileHolder<*>>().apply {
            primaryAction?.let(::add)
            add(topBarTitle)
            topBarSubtitle?.let(::addAll)
            topBarActions?.let(::addAll)
        }

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() },
        primaryAction = primaryAction?.get(),
        topBar = tile.topBar.copy(
            title = topBarTitle.get(),
            actions = topBarActions?.immutableMapTo { it.get() },
            subtitle = topBarSubtitle?.immutableMapTo { it.get() },
        )
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is AdaptiveNavigationTileEvents.OnItemClicked -> {
                tile = tile.copy(selectedEntryId = event.entryId)
            }
        }
    }
}
