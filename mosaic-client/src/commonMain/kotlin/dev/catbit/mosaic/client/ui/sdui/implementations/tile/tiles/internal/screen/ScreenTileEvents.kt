package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface ScreenTileEvents : TileEvent {
    data class OnDismissOverlayFinished(val overlayId: String) : ScreenTileEvents
}