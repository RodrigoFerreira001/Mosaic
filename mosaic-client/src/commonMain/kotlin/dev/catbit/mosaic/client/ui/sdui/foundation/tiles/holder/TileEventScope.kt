package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

class TileEventScope(
    private val builderScope: BuilderScope
) {
    fun buildTileHolder(tileSchema: TileSchema): TileHolder<*> =
        builderScope.buildTileHolder(tileSchema)

    fun buildEventHolder(eventSchema: EventSchema): EventHolder<*> =
        builderScope.buildEventHolder(eventSchema)

    fun List<EventSchema>.buildEventHolders(): MutableList<EventHolder<*>> =
        with(builderScope) { buildEventHolders() }

    fun List<TileSchema>.buildTileHolders(): MutableList<TileHolder<*>> =
        with(builderScope) { buildTileHolders() }
}