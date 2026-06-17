package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pager

import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastData

sealed interface PagerTileScreenTilesBroadcastData : ScreenTilesBroadcastData {

    val smoothly: Boolean

    data class ScrollToBegin(
        override val tileId: String,
        override val smoothly: Boolean
    ) : PagerTileScreenTilesBroadcastData

    data class ScrollToPreviousPage(
        override val tileId: String,
        override val smoothly: Boolean
    ) : PagerTileScreenTilesBroadcastData

    data class ScrollToNextPage(
        override val tileId: String,
        override val smoothly: Boolean
    ) : PagerTileScreenTilesBroadcastData

    data class ScrollToEnd(
        override val tileId: String,
        override val smoothly: Boolean
    ) : PagerTileScreenTilesBroadcastData
}