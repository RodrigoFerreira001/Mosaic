package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.search.search_bar

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

sealed interface SearchBarTileEvents : TileEvent {
    data object OnQueryCleared : SearchBarTileEvents
    data class OnQueryChanged(val query: String) : SearchBarTileEvents
}