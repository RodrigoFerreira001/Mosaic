package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema

object AddTilesEventRunner : EventRunner<AddTilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: AddTilesEventSchema) {

        // Todo implementar aqui um mecanismo de geração de ID, por exemplo, procurar por tiles/events com [GENERATE#1]

        tilesEditor.addTiles(
            tileSchemas = event.tiles,
            groupingTileId = event.groupingTileId
        )
    }
}