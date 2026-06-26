package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.app_bars.bottom_app_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.BottomAppBarTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class BottomAppBarTileHolder(
    override val id: String,
    override var tile: BottomAppBarTileSchema,
    override val events: MutableList<EventHolder<*>>,
    private val actions: List<TileHolder<*>>,
    private val floatingActionButton: TileHolder<*>?,
) : TileHolder<BottomAppBarTileSchema>() {

    override val tiles = actions.toMutableList().apply { floatingActionButton?.let(::add) }

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() },
        actions = actions.immutableMapTo { it.get() },
        floatingActionButton = floatingActionButton?.get()
    )
}
