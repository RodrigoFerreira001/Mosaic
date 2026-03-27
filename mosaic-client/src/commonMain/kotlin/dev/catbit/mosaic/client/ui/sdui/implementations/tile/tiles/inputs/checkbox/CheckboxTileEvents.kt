package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.checkbox

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface CheckboxTileEvents : TileEvent {
    data class OnCheckChanged(val isChecked: Boolean): CheckboxTileEvents
}