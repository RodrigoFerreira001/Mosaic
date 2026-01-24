package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData

sealed interface ScreenTileBroadcastData : BroadcastData {
    data class OnDisplayBottomSheetRequested(
        val isCancellable: Boolean,
        val fill: Boolean,
    ): ScreenTileBroadcastData
    data object DismissBottomSheet : ScreenTileBroadcastData
    data class OnDisplayDialogRequested(
        val isCancellable: Boolean,
        val usePlatformDefaultWidth: Boolean
    ): ScreenTileBroadcastData
    data object DismissDialog : ScreenTileBroadcastData
    data class DisplaySnackbar(val message: String) : ScreenTileBroadcastData
    data object DisplayNavigationDrawer : ScreenTileBroadcastData
    data object DismissNavigationDrawer : ScreenTileBroadcastData
}