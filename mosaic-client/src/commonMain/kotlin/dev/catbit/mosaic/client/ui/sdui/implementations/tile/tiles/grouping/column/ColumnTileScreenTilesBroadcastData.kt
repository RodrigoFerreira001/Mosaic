package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastData

sealed interface ColumnTileScreenTilesBroadcastData : ScreenTilesBroadcastData {

    val smoothly: Boolean

    data class ScrollToTop(
        override val tileId: String,
        override val smoothly: Boolean
    ) : ColumnTileScreenTilesBroadcastData

    data class ScrollTo(
        override val tileId: String,
        override val smoothly: Boolean,
        val index: Int,
    ) : ColumnTileScreenTilesBroadcastData

    data class ScrollToBottom(
        override val tileId: String,
        override val smoothly: Boolean
    ) : ColumnTileScreenTilesBroadcastData
}