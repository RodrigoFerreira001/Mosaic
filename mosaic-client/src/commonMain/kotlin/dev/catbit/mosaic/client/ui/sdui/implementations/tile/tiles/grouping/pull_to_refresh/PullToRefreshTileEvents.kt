package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pull_to_refresh

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface PullToRefreshTileEvents : TileEvent {
    data object OnRefreshStart : PullToRefreshTileEvents
    data object StopRefreshing : PullToRefreshTileEvents
}