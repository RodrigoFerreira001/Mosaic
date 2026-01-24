package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface MenuTileEvents : TileEvent {
    data object OnToggleMenu : MenuTileEvents
}