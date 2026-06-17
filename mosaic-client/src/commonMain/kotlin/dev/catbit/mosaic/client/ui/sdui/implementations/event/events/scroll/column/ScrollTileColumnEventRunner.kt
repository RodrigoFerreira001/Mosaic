package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column.ColumnTileScreenTilesBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object ScrollTileColumnEventRunner : EventRunner<ScrollColumnTileEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: ScrollColumnTileEventSchema) {
        broadcastData(
            when (val where = event.where) {
                ScrollColumnTileEventSchema.Where.Top -> ColumnTileScreenTilesBroadcastData.ScrollToTop(
                    tileId = event.tileId,
                    smoothly = event.smoothly
                )

                is ScrollColumnTileEventSchema.Where.To -> ColumnTileScreenTilesBroadcastData.ScrollTo(
                    tileId = event.tileId,
                    index = where.index,
                    smoothly = event.smoothly
                )

                ScrollColumnTileEventSchema.Where.Bottom -> ColumnTileScreenTilesBroadcastData.ScrollToBottom(
                    tileId = event.tileId,
                    smoothly = event.smoothly
                )
            }
        )
        onTrigger(EventTriggers.onSuccess())
    }
}