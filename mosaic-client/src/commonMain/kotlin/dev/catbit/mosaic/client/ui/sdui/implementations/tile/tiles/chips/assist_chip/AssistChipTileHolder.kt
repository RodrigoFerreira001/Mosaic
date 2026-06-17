package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.assist_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.AssistChipTileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class AssistChipTileHolder(
    override val id: String,
    override var tile: AssistChipTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<AssistChipTileSchema>() {

    override fun get() = tile.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
