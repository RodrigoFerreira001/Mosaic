package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.date_picker

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface DatePickerTileEvents : TileEvent {
    data class OnDateConfirmed(val date: String) : DatePickerTileEvents
    object OnDatePickerToggle : DatePickerTileEvents
    object OnDatePickerDismissRequest : DatePickerTileEvents
}
