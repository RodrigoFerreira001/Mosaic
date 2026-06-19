package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.update_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema.Update.UpdateData
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object UpdateTilesEventRunner : EventRunner<UpdateTilesEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: UpdateTilesEventSchema) {
        var anyErrorOccurred = false

        event.updates.forEach { update ->
            val data = when (val updateData = update.updateData) {
                is UpdateData.Incoming -> incomingData.asMapAny() ?: return@forEach
                is UpdateData.Inline -> updateData.data
            }

            tilesEditor.updateTile(
                tileId = update.tileId,
                updateData = data,
            ).onFailure { throwable ->
                anyErrorOccurred = true
                logError(tag = "UpdateTilesEventRunner", throwable = throwable)
            }
        }

        if (anyErrorOccurred) {
            onTrigger(EventTriggers.onFailure())
        } else {
            onTrigger(EventTriggers.onTilesUpdated())
            onTrigger(EventTriggers.onSuccess())
        }
    }
}
