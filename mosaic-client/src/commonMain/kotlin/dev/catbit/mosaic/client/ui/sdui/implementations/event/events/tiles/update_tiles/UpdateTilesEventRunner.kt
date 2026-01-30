package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.update_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema

object UpdateTilesEventRunner : EventRunner<UpdateTilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: UpdateTilesEventSchema) {
        event.updates.forEach { update ->
            tilesEditor.updateTile(
                tileId = update.tileId,
                updateData = update.data
            )
        }
    }
}
