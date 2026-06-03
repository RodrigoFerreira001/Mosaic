package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.update_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object UpdateTilesEventRunner : EventRunner<UpdateTilesEventSchema> {
    override fun EventRunningScope.runEvent(event: UpdateTilesEventSchema) {
        val updates = event.updates
        var remaining = updates.size

        fun onAllDone() {
            remaining--
            if (remaining == 0) {
                onTrigger(EventTriggers.onTilesUpdated())
                onTrigger(EventTriggers.onSuccess())
            }
        }

        updates.forEach { update ->
            tilesEditor.updateTile(
                tileId = update.tileId,
                updateData = update.data,
                onError = {
                    onTrigger(EventTriggers.onFailure(), data = it)
                    logError(
                        tag = "UpdateTilesEventRunner",
                        throwable = it
                    )
                },
                onSuccess = { onAllDone() }
            )
        }
    }
}
