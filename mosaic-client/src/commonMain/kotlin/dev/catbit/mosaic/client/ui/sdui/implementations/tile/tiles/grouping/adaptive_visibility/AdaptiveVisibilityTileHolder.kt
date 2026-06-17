package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.adaptive_visibility

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class AdaptiveVisibilityTileHolder(
    override val id: String,
    override var tile: AdaptiveVisibilityTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>
) : TileHolder<AdaptiveVisibilityTileSchema>() {

    override fun get() = tile.copy(
        tiles = tiles.immutableMapTo { it.get() },
        events = events?.immutableMapTo { it.get() }
    )
}
