package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState

sealed interface State {
    data object Loading : State
    data class Displaying(
        val tiles: List<TileUIState>
    ): State
    data object Failure : State
}

sealed interface Event {
    data class OnUIEvent(val event: UIEvent): Event
    data object OnTryAgainClick: Event
}

sealed interface Effect