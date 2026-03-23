package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.pager

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pager.PagerTileBroadcastData
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager.ScrollPagerTileEventSchema

object ScrollTilePagerEventRunner : EventRunner<ScrollPagerTileEventSchema> {

    override fun EventRunningScope.runEvent(event: ScrollPagerTileEventSchema) {
        broadcastData(
            when (event.where) {
                ScrollPagerTileEventSchema.Where.Begin -> PagerTileBroadcastData.ScrollToBegin(event.tileId)
                ScrollPagerTileEventSchema.Where.End -> PagerTileBroadcastData.ScrollToEnd(event.tileId)
                ScrollPagerTileEventSchema.Where.NextPage -> PagerTileBroadcastData.ScrollToNextPage(event.tileId)
                ScrollPagerTileEventSchema.Where.PreviousPage -> PagerTileBroadcastData.ScrollToPreviousPage(event.tileId)
            }
        )
    }
}