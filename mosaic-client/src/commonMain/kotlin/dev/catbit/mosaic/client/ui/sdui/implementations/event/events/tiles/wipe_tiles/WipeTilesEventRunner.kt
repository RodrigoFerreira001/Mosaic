package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.wipe_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.tiles.RemoveTilesEventModel
import dev.catbit.mosaic.core.data.event.events.tiles.WipeTilesEventModel

object WipeTilesEventRunner : EventRunner<WipeTilesEventModel> {
    override fun EventRunningScope.runEvent(event: WipeTilesEventModel) {
        tilesEditor.wipeTiles(
            groupingTileId = event.groupingTileId,
        )
    }
}