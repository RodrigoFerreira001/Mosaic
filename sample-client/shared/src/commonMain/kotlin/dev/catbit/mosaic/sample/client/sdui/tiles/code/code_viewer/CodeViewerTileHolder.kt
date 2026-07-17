package dev.catbit.mosaic.sample.client.sdui.tiles.code.code_viewer

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.extensions.immutableMapTo
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema

class CodeViewerTileHolder(
    override val id: String,
    override var tile: CodeViewerTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<CodeViewerTileSchema>() {

    override fun get() = tile.copy(
        events = events.immutableMapTo { it.get() }
    )
}
