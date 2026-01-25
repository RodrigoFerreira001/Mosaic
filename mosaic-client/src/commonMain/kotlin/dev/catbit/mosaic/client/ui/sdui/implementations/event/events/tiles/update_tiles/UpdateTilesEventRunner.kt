package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.update_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.tiles.UpdateTilesEventModel

object UpdateTilesEventRunner : EventRunner<UpdateTilesEventModel> {
    override suspend fun EventRunningScope.runEvent(event: UpdateTilesEventModel) {
        event.updates.forEach { update ->
            tilesEditor.updateTile(
                tileId = update.tileId,
                updateData = update.data
            )
        }
    }
}
