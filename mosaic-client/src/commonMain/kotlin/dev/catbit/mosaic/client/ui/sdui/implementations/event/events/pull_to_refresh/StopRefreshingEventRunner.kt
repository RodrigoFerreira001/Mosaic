package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.pull_to_refresh

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pull_to_refresh.PullToRefreshTileEvents
import dev.catbit.mosaic.core.data.schemas.event.events.pull_to_refresh.StopRefreshingEventSchema

object StopRefreshingEventRunner : EventRunner<StopRefreshingEventSchema> {

    override fun EventRunningScope.runEvent(event: StopRefreshingEventSchema) {
        tilesEventDispatcher.onEvent(
            tileId = event.tileId,
            event = PullToRefreshTileEvents.StopRefreshing
        )
    }
}