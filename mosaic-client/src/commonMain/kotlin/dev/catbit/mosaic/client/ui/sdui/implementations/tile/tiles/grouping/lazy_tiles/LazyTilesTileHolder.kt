package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyTilesTileSchema

class LazyTilesTileHolder(
    override val id: String,
    override var tile: LazyTilesTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    val failureTiles: MutableList<TileHolder<*>>,
    val placeholderTiles: MutableList<TileHolder<*>>
) : TileHolder<LazyTilesTileSchema>() {

    private var internalTiles: MutableList<TileHolder<*>>? = null

    override val tiles get() = mutableListOf<TileHolder<*>>().apply {
        addAll(placeholderTiles)
        addAll(failureTiles)
        internalTiles?.let(::addAll)
    }

    override fun get() = tile.copy(
        failureTiles = failureTiles.map { it.get() },
        placeholderTiles = placeholderTiles.map { it.get() },
        events = events?.map { it.get() },
        tiles = internalTiles?.map { it.get() }
    )

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is LazyTilesTileEvents.OnTilesLoadFailure -> {
                tile = tile.copy(
                    isFailureState = true
                )
            }

            is LazyTilesTileEvents.OnTilesLoadedSuccessfully -> {
                with(event.tiles.buildTileHolders()) {
                    internalTiles = this
                }
            }

            is LazyTilesTileEvents.OnReloadTiles -> {
                internalTiles = null
                tile = tile.copy(isFailureState = false)
            }
        }
    }
}
