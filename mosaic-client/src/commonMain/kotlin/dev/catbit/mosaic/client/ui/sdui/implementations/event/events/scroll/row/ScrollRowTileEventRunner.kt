package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.row

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row.RowTileScreenTilesBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.row.ScrollRowTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object ScrollRowTileEventRunner : EventRunner<ScrollRowTileEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: ScrollRowTileEventSchema) {
        broadcastData(
            when (val where = event.where) {
                ScrollRowTileEventSchema.Where.Start -> RowTileScreenTilesBroadcastData.ScrollToStart(
                    tileId = event.tileId,
                    smoothly = event.smoothly
                )

                is ScrollRowTileEventSchema.Where.To -> RowTileScreenTilesBroadcastData.ScrollTo(
                    tileId = event.tileId,
                    index = where.index,
                    smoothly = event.smoothly
                )

                ScrollRowTileEventSchema.Where.End -> RowTileScreenTilesBroadcastData.ScrollToEnd(
                    tileId = event.tileId,
                    smoothly = event.smoothly
                )
            }
        )
        onTrigger(EventTriggers.onSuccess())
    }
}
