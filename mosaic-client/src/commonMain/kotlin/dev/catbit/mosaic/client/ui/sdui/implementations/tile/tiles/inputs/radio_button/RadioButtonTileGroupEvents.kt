package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.radio_button

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent

sealed interface RadioButtonTileGroupEvents : TileGroupEvent {
    data class OnRadioSelected(
        val selectedTileId: String,
        val groupId: String
    ) : RadioButtonTileGroupEvents
}