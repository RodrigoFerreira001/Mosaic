package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface NavigationBarTileEvents : TileEvent {
    data class OnItemClicked(val itemId: String) : NavigationBarTileEvents
}