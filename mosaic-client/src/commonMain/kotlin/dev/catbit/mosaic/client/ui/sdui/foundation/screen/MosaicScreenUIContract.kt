package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.core.data.tile.TileModel

sealed interface State {
    data object Loading : State
    data class Displaying(
        val rootTile: TileModel
    ) : State

    data object Failure : State
}

sealed interface Event {
    data class OnUIEvent(val event: UIEvent) : Event
    data object OnTryAgainClick : Event
}

sealed interface Effect