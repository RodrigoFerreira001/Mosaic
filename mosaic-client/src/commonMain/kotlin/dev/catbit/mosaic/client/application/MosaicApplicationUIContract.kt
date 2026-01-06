package dev.catbit.mosaic.client.application

import dev.catbit.mosaic.client.ui.sdui.foundation.graph.GraphUIState

sealed interface State {
    data object Loading : State
    data class Displaying(
        val graph: GraphUIState
    ): State
    data class Failure(
        val title: String,
        val details: String
    ) : State
}

sealed interface Event {
    data object OnTryAgainClick: Event
}
sealed interface Effect