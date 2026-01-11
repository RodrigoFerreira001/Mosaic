package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.replace_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.tiles.ReplaceTilesEventModel

object ReplaceTilesEventRunner : EventRunner<ReplaceTilesEventModel> {
    override suspend fun EventRunningScope.runEvent(event: ReplaceTilesEventModel) {
        tilesEditor.replaceTiles(
            groupingTileId = event.groupingTileId,
            tileModels = event.tiles
        )
    }
}