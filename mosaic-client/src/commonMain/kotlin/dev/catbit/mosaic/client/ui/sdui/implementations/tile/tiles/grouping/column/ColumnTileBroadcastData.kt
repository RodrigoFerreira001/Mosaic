package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData

sealed interface ColumnTileBroadcastData : BroadcastData {
    val tileId: String
    val smooth: Boolean

    data class ScrollToTop(
        override val tileId: String,
        override val smooth: Boolean
    ) : ColumnTileBroadcastData

    data class ScrollTo(
        override val tileId: String,
        override val smooth: Boolean,
        val index: Int,
    ) : ColumnTileBroadcastData

    data class ScrollToBottom(
        override val tileId: String,
        override val smooth: Boolean
    ) : ColumnTileBroadcastData
}