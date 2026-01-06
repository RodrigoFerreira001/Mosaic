package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.tiles.AddTilesEventModel

object AddTilesEventRunner : EventRunner<AddTilesEventModel> {
    override fun EventRunningScope.runEvent(event: AddTilesEventModel) {
        tilesEditor.addTiles(
            tileModels = event.tiles,
            groupingTileId = event.groupingTileId
        )
    }
}