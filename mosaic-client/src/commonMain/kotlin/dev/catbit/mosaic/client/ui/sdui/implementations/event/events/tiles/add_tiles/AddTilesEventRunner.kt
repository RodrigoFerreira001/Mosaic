package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.tiles.AddTilesEventModel

object AddTilesEventRunner : EventRunner<AddTilesEventModel> {
    override suspend fun EventRunningScope.runEvent(event: AddTilesEventModel) {

        // Todo implementar aqui um mecanismo de geração de ID, por exemplo, procurar por tiles/events com [GENERATE#1]

        tilesEditor.addTiles(
            tileModels = event.tiles,
            groupingTileId = event.groupingTileId
        )
    }
}