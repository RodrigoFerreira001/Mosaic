package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pager

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData

sealed interface PagerTileBroadcastData : BroadcastData {
    data class ScrollToBegin(override val tileId: String) : PagerTileBroadcastData
    data class ScrollToPreviousPage(override val tileId: String) : PagerTileBroadcastData
    data class ScrollToNextPage(override val tileId: String) : PagerTileBroadcastData
    data class ScrollToEnd(override val tileId: String) : PagerTileBroadcastData
}