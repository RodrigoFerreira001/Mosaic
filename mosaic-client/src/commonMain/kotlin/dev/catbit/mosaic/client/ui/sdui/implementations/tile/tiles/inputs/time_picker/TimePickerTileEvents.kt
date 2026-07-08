package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.time_picker

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface TimePickerTileEvents : TileEvent {
    data class OnTimeConfirmed(val time: String) : TimePickerTileEvents
    object OnTimePickerToggle : TimePickerTileEvents
    object OnTimePickerDismissRequest : TimePickerTileEvents
}
