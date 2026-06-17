package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.remove_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.RemoveTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object RemoveTilesEventRunner : EventRunner<RemoveTilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: RemoveTilesEventSchema) {
        tilesEditor.removeTiles(
            tileIds = event.tileIds,
            groupingTileId = event.groupingTileId,
        ).onFailure { throwable ->
            onTrigger(EventTriggers.onFailure(), data = throwable)
            logError(tag = "RemoveTilesEventRunner", throwable = throwable)
        }.onSuccess {
            onTrigger(EventTriggers.onSuccess())
        }
    }
}