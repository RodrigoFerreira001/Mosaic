package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.row

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row.RowTileBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.row.ScrollRowTileEventSchema

object ScrollRowTileEventRunner : EventRunner<ScrollRowTileEventSchema> {
    override fun EventRunningScope.runEvent(event: ScrollRowTileEventSchema) {
        broadcastData(
            when (val where = event.where) {
                ScrollRowTileEventSchema.Where.Start -> RowTileBroadcastData.ScrollToStart(
                    tileId = event.tileId,
                    smoothly = event.smoothly
                )

                is ScrollRowTileEventSchema.Where.To -> RowTileBroadcastData.ScrollTo(
                    tileId = event.tileId,
                    index = where.index,
                    smoothly = event.smoothly
                )

                ScrollRowTileEventSchema.Where.End -> RowTileBroadcastData.ScrollToEnd(
                    tileId = event.tileId,
                    smoothly = event.smoothly
                )
            }
        )
    }
}
