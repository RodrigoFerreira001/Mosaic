package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

sealed interface LazyTilesTileEvents : TileEvent{
    data class OnTilesLoadedSuccessfully(
        val tiles: List<TileSchema>
    ) : LazyTilesTileEvents

    data object OnTilesLoadFailure : LazyTilesTileEvents

    data object OnReloadTiles : LazyTilesTileEvents
}