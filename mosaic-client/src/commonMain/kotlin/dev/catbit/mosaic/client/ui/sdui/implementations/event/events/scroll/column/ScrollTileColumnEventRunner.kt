package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column.ColumnTileBroadcastData
import dev.catbit.mosaic.core.data.event.events.scroll.column.ScrollTileColumnEventModel

object ScrollTileColumnEventRunner : EventRunner<ScrollTileColumnEventModel> {

    override fun EventRunningScope.runEvent(event: ScrollTileColumnEventModel) {
        broadcastData(
            when (val where = event.where) {
                ScrollTileColumnEventModel.Where.Top -> ColumnTileBroadcastData.ScrollToTop(
                    tileId = event.tileId,
                    smooth = event.smooth
                )

                is ScrollTileColumnEventModel.Where.To -> ColumnTileBroadcastData.ScrollTo(
                    tileId = event.tileId,
                    index = where.index,
                    smooth = event.smooth
                )

                ScrollTileColumnEventModel.Where.Bottom -> ColumnTileBroadcastData.ScrollToBottom(
                    tileId = event.tileId,
                    smooth = event.smooth
                )
            }
        )
    }
}