package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState

sealed interface State {
    data object Loading : State
    data class Displaying(
        val screenTiles: List<TileUIState>,
        val bottomSheetUIState: BottomSheetUIState? = null,
        val dialogUIState: DialogUIState? = null,
        val navigationDrawerUIState: NavigationDrawerUIState? = null,
    ) : State {

        data class DialogUIState(
            val isCancellable: Boolean,
            val usePlatformDefaultWidth: Boolean,
            val tiles: List<TileUIState>
        )

        data class BottomSheetUIState(
            val isCancellable: Boolean,
            val tiles: List<TileUIState>
        )

        data class NavigationDrawerUIState(
            val tiles: List<TileUIState>
        )
    }

    data object Failure : State
}

sealed interface Event {
    data class OnUIEvent(val event: UIEvent) : Event
    data object OnCloseDialogRequested: Event
    data object OnCloseBottomSheetFinished : Event
    data object OnTryAgainClick : Event
}

sealed interface Effect {
    // TODO Add more info and behaviors, like click
    data class OnDisplaySnackbar(
        val message: String
    ) : Effect
    data object OnCloseBottomSheetRequested : Effect
    data object OnCloseNavigationDrawerSheetRequested : Effect
}