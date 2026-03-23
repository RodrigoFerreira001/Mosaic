package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData

sealed interface RowTileBroadcastData : BroadcastData {

    val smoothly: Boolean

    data class ScrollToStart(
        override val tileId: String,
        override val smoothly: Boolean
    ) : RowTileBroadcastData

    data class ScrollTo(
        override val tileId: String,
        override val smoothly: Boolean,
        val index: Int,
    ) : RowTileBroadcastData

    data class ScrollToEnd(
        override val tileId: String,
        override val smoothly: Boolean
    ) : RowTileBroadcastData
}