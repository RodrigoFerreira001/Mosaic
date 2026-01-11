package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.remove_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.tiles.RemoveTilesEventModel

object RemoveTilesEventRunner : EventRunner<RemoveTilesEventModel> {
    override suspend fun EventRunningScope.runEvent(event: RemoveTilesEventModel) {
        tilesEditor.removeTiles(
            groupingTileId = event.groupingTileId,
            tileIds = event.tileIds
        )
    }
}