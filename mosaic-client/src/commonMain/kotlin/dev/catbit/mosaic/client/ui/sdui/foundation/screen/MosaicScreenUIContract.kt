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
            val tiles: List<TileUIState>
        )

        data class BottomSheetUIState(
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
    data object OnCloseBottomSheetFinished : Event
    data object OnCloseDialogFinished : Event
    data object OnTryAgainClick : Event
}

sealed interface Effect {
    data class OnDisplaySnackbar(
        val message: String
    ) : Effect

    data object OnCloseSnackbarRequested : Effect

    data class OnDisplayBottomSheetRequested(
        val isCancellable: Boolean,
        val fill: Boolean,
    ) : Effect

    data object OnCloseBottomSheetRequested : Effect

    data class OnDisplayDialogRequested(
        val isCancellable: Boolean,
        val usePlatformDefaultWidth: Boolean
    ) : Effect

    data object OnCloseDialogRequested : Effect

    data object OnDisplayNavigationDrawerSheetRequested : Effect
    data object OnCloseNavigationDrawerSheetRequested : Effect
}