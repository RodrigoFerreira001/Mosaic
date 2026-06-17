package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.menu.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu.MenuTileEvents
import dev.catbit.mosaic.core.data.schemas.event.events.menu.ToggleMenuEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object ToggleMenuEventRunner : EventRunner<ToggleMenuEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: ToggleMenuEventSchema) {
        tilesEventDispatcher.onEvent(
            tileId = event.menuId,
            event = MenuTileEvents.OnToggleMenu,
        ).onFailure { throwable ->
            onTrigger(EventTriggers.onFailure(), data = throwable)
            logError(tag = "ToggleMenuEventRunner", throwable = throwable)
        }.onSuccess {
            onTrigger(EventTriggers.onSuccess())
        }
    }
}