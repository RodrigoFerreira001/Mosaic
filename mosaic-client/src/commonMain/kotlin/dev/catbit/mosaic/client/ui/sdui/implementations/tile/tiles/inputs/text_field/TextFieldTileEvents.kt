package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface TextFieldTileEvents : TileEvent {
    data class OnTextChange(val newValue: String) : TextFieldTileEvents
}