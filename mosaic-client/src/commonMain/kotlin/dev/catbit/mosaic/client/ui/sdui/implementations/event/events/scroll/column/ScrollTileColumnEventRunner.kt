package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column.ColumnTileBroadcastData
import dev.catbit.mosaic.core.data.event.events.scroll.column.ScrollTileColumnEventModel

object ScrollTileColumnEventRunner : EventRunner<ScrollTileColumnEventModel> {

    override suspend fun EventRunningScope.runEvent(event: ScrollTileColumnEventModel) {
        broadcastData(
            when (val where = event.where) {
                ScrollTileColumnEventModel.Where.Top -> ColumnTileBroadcastData.ScrollToTop(
                    tileId = event.tileId,
                    smoothly = event.smoothly
                )

                is ScrollTileColumnEventModel.Where.To -> ColumnTileBroadcastData.ScrollTo(
                    tileId = event.tileId,
                    index = where.index,
                    smoothly = event.smoothly
                )

                ScrollTileColumnEventModel.Where.Bottom -> ColumnTileBroadcastData.ScrollToBottom(
                    tileId = event.tileId,
                    smoothly = event.smoothly
                )
            }
        )
    }
}