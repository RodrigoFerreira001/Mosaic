package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.filter_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface FilterChipTileEvents : TileEvent {
    data class OnCheckChanged(val isSelected: Boolean) : FilterChipTileEvents
}
