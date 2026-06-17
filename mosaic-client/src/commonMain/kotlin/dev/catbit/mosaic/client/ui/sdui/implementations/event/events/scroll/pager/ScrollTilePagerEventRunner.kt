package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.pager

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pager.PagerTileScreenTilesBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager.ScrollPagerTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object ScrollTilePagerEventRunner : EventRunner<ScrollPagerTileEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: ScrollPagerTileEventSchema) {
        broadcastData(
            with(event) {
                when (event.where) {
                    ScrollPagerTileEventSchema.Where.Begin -> PagerTileScreenTilesBroadcastData.ScrollToBegin(tileId, smoothly)
                    ScrollPagerTileEventSchema.Where.End -> PagerTileScreenTilesBroadcastData.ScrollToEnd(tileId, smoothly)
                    ScrollPagerTileEventSchema.Where.NextPage -> PagerTileScreenTilesBroadcastData.ScrollToNextPage(tileId, smoothly)
                    ScrollPagerTileEventSchema.Where.PreviousPage -> PagerTileScreenTilesBroadcastData.ScrollToPreviousPage(tileId, smoothly)
                }
            }
        )
        onTrigger(EventTriggers.onSuccess())
    }
}