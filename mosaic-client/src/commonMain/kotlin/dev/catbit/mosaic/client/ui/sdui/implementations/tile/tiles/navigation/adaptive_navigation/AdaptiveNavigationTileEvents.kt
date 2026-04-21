package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.adaptive_navigation

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface AdaptiveNavigationTileEvents : TileEvent {
    data class OnItemClicked(val entryId: String) : AdaptiveNavigationTileEvents
}
