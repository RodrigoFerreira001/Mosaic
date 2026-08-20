package dev.catbit.mosaic.client.application

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.GraphUIState

/**
 * [MosaicApplicationStateHolder]'s own UI state — the 3 states `MosaicApplication`'s root content
 * switches on ([MosaicApplicationContent]) before any screen exists yet.
 */
@Immutable
sealed interface State {
    /** Fetching the initial graph — renders the app's `appSplash`. */
    data object Loading : State
    /** The initial graph loaded successfully — renders the real navigation UI (`NavDisplay`).
     * @property graph the loaded navigation graph. */
    data class Displaying(
        val graph: GraphUIState
    ): State
    /** Fetching the initial graph failed — renders the app-level failure screen with a retry button.
     * @property loading whether a retry is currently in flight (disables the retry button, shows a
     * spinner). */
    data class Failure(
        val loading: Boolean = false
    ) : State
}

/** UI events [MosaicApplicationStateHolder] handles. */
@Immutable
sealed interface Event {
    /** The user tapped the retry button on the app-level failure screen. */
    data object OnTryAgainClick: Event
}

/** One-shot effects [MosaicApplicationStateHolder] could emit — currently empty, no case defined
 * yet. */
@Immutable
sealed interface Effect