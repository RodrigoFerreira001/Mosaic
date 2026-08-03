package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.progress.linear_progress

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.LinearProgressIndicatorTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class LinearProgressIndicatorTileHolder(
    override val id: String,
    override var tile: LinearProgressIndicatorTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<LinearProgressIndicatorTileSchema>() {

    override fun getTileSchema() = tile.copy(
        events = events.immutableMapTo { it.get() }
    )
}
