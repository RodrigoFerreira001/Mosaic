package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pager

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData

sealed interface PagerTileBroadcastData : BroadcastData {

    val smoothly: Boolean

    data class ScrollToBegin(
        override val tileId: String,
        override val smoothly: Boolean
    ) : PagerTileBroadcastData

    data class ScrollToPreviousPage(
        override val tileId: String,
        override val smoothly: Boolean
    ) : PagerTileBroadcastData

    data class ScrollToNextPage(
        override val tileId: String,
        override val smoothly: Boolean
    ) : PagerTileBroadcastData

    data class ScrollToEnd(
        override val tileId: String,
        override val smoothly: Boolean
    ) : PagerTileBroadcastData
}