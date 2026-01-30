package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.remove_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.RemoveTilesEventSchema

object RemoveTilesEventRunner : EventRunner<RemoveTilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: RemoveTilesEventSchema) {
        tilesEditor.removeTiles(
            groupingTileId = event.groupingTileId,
            tileIds = event.tileIds
        )
    }
}