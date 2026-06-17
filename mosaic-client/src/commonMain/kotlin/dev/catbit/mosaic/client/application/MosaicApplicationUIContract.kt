package dev.catbit.mosaic.client.application

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.GraphUIState

@Immutable
sealed interface State {
    data object Loading : State
    data class Displaying(
        val graph: GraphUIState
    ): State
    data class Failure(
        val loading: Boolean = false
    ) : State
}

@Immutable
sealed interface Event {
    data object OnTryAgainClick: Event
}

@Immutable
sealed interface Effect