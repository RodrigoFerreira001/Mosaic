package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object AddTilesEventRunner : EventRunner<AddTilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: AddTilesEventSchema) {

        tilesEditor.addTiles(
            tileSchemas = event.tiles,
            groupingTileId = event.groupingTileId,
        ).onFailure { throwable ->
            onTrigger(EventTriggers.onFailure(), data = throwable)
            logError(tag = "AddTilesEventRunner", throwable = throwable)
        }.onSuccess {
            onTrigger(EventTriggers.onSuccess())
        }
    }
}