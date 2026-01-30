package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column.ColumnTileBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema

object ScrollTileColumnEventRunner : EventRunner<ScrollColumnTileEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: ScrollColumnTileEventSchema) {
        broadcastData(
            when (val where = event.where) {
                ScrollColumnTileEventSchema.Where.Top -> ColumnTileBroadcastData.ScrollToTop(
                    tileId = event.tileId,
                    smoothly = event.smoothly
                )

                is ScrollColumnTileEventSchema.Where.To -> ColumnTileBroadcastData.ScrollTo(
                    tileId = event.tileId,
                    index = where.index,
                    smoothly = event.smoothly
                )

                ScrollColumnTileEventSchema.Where.Bottom -> ColumnTileBroadcastData.ScrollToBottom(
                    tileId = event.tileId,
                    smoothly = event.smoothly
                )
            }
        )
    }
}