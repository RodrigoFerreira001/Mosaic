package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.dropdown_list

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface DropdownListTileEvents : TileEvent {
    data class OnItemSelected(val selectedOptionId: String) : DropdownListTileEvents
    object OnDropdownListToggle : DropdownListTileEvents
    object OnDropdownListDismissRequest : DropdownListTileEvents
}
