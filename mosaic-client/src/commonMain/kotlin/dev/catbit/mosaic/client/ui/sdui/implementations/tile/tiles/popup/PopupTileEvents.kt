package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.popup

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface PopupTileEvents : TileEvent {
    data object OnTogglePopup : PopupTileEvents
}
