package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.tabs

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface TabsTileEvents : TileEvent {
    data class OnTabClicked(val tabId: String) : TabsTileEvents
}