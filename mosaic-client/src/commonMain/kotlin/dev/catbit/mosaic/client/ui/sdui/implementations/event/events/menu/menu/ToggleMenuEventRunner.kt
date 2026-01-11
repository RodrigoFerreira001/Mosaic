package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.menu.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu.MenuTileEvent
import dev.catbit.mosaic.core.data.event.events.menu.ToggleMenuEventModel

object ToggleMenuEventRunner : EventRunner<ToggleMenuEventModel> {
    override suspend fun EventRunningScope.runEvent(event: ToggleMenuEventModel) {
        tilesEventDispatcher.onEvent(
            tileId = event.menuId,
            event = MenuTileEvent.OnToggleMenu
        )
    }
}