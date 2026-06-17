package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.progress.circular_progress

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.CircularProgressIndicatorTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class CircularProgressIndicatorTileHolder(
    override val id: String,
    override var tile: CircularProgressIndicatorTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<CircularProgressIndicatorTileSchema>() {

    override fun get() = tile.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
