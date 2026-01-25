package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent

interface TilesEventDispatcher {
    fun onEvent(
        tileId: String,
        event: TileEvent
    )
}