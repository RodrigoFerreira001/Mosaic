package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.replace_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReplaceTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object ReplaceTilesEventRunner : EventRunner<ReplaceTilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: ReplaceTilesEventSchema) {
        tilesEditor.replaceTiles(
            tileSchemas = event.tiles,
            groupingTileId = event.groupingTileId,
        ).onFailure { throwable ->
            onTrigger(EventTriggers.onFailure(), data = throwable)
            logError(tag = "ReplaceTilesEventRunner", throwable = throwable)
        }.onSuccess {
            onTrigger(EventTriggers.onSuccess())
        }
    }
}