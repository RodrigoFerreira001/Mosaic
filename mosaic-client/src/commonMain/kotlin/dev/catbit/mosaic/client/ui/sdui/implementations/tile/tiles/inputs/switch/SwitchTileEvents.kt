package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.switch

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

interface SwitchTileEvents : TileEvent {
    data class OnCheckChanged(val isChecked: Boolean): SwitchTileEvents
}