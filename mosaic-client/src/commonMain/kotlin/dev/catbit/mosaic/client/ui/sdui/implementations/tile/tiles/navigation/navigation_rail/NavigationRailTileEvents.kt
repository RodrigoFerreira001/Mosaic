package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_rail

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface NavigationRailTileEvents : TileEvent {
    data class OnItemClicked(val itemId: String) : NavigationRailTileEvents
}