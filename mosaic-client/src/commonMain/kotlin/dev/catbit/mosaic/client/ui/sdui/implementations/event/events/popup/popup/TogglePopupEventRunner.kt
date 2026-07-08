package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.popup.popup

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.popup.PopupTileEvents
import dev.catbit.mosaic.core.data.schemas.event.events.popup.TogglePopupEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object TogglePopupEventRunner : EventRunner<TogglePopupEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: TogglePopupEventSchema) {
        tilesEventDispatcher.onEvent(
            tileId = event.popupId,
            event = PopupTileEvents.OnTogglePopup,
        ).onFailure { throwable ->
            onTrigger(EventTriggers.onFailure(), data = throwable)
            logError(tag = "TogglePopupEventRunner", throwable = throwable)
        }.onSuccess {
            onTrigger(EventTriggers.onSuccess())
        }
    }
}
