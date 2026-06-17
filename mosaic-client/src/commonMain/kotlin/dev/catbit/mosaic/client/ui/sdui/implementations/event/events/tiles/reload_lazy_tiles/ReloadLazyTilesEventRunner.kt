package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.reload_lazy_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_tiles.LazyTilesTileEvents
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReloadLazyTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object ReloadLazyTilesEventRunner : EventRunner<ReloadLazyTilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: ReloadLazyTilesEventSchema) {
        tilesEventDispatcher.onEvent(
            tileId = event.lazyTileId,
            event = LazyTilesTileEvents.OnReloadTiles,
        )
            .onSuccess {
                onTrigger(EventTriggers.onSuccess())
            }
            .onFailure {
                onTrigger(EventTriggers.onFailure(), data = it)
            }
    }
}
