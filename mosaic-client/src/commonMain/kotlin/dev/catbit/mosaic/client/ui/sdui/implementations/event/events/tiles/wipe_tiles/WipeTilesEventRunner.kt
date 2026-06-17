package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.wipe_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.WipeTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object WipeTilesEventRunner : EventRunner<WipeTilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: WipeTilesEventSchema) {
        tilesEditor.wipeTiles(
            groupingTileId = event.groupingTileId,
        ).onFailure { throwable ->
            onTrigger(EventTriggers.onFailure(), data = throwable)
            logError(tag = "WipeTilesEventRunner", throwable = throwable)
        }.onSuccess {
            onTrigger(EventTriggers.onSuccess())
        }
    }
}