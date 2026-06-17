package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.input_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface InputChipTileEvents : TileEvent {
    data class OnCheckChanged(val isSelected: Boolean) : InputChipTileEvents
}
