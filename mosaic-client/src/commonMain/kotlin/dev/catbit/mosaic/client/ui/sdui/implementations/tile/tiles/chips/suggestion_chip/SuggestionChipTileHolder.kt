package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.suggestion_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.SuggestionChipTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class SuggestionChipTileHolder(
    override val id: String,
    override var tile: SuggestionChipTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<SuggestionChipTileSchema>() {

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() }
    )
}
