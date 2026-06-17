package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.app_bars.top_app_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.TopAppBarTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class TopAppBarTileHolder(
    override val id: String,
    override var tile: TopAppBarTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    private val title: TileHolder<*>,
    private val navigationIcon: TileHolder<*>?,
    private val actions: List<TileHolder<*>>?
) : TileHolder<TopAppBarTileSchema>() {

    override val tiles: MutableList<TileHolder<*>> =
        actions.orEmpty().toMutableList().apply {
            add(title)
            navigationIcon?.let(::add)
        }

    override fun get() = tile.copy(
        events = events?.immutableMapTo { it.get() },
        title = title.get(),
        navigationIcon = navigationIcon?.get(),
        actions = actions?.immutableMapTo { it.get() },
    )
}
