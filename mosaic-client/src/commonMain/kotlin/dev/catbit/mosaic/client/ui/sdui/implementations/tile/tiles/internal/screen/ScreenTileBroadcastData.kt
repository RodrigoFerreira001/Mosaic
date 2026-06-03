package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import androidx.compose.material3.SnackbarDuration
import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData

sealed interface ScreenTileBroadcastData : BroadcastData {
    data class OnDisplayBottomSheetRequested(
        override val tileId: String? = null,
        val isCancellable: Boolean,
        val fill: Boolean,
    ) : ScreenTileBroadcastData

    data class DismissBottomSheet(
        override val tileId: String? = null,
    ) : ScreenTileBroadcastData

    data class OnDisplayDialogRequested(
        override val tileId: String? = null,
        val isCancellable: Boolean,
        val usePlatformDefaultWidth: Boolean
    ) : ScreenTileBroadcastData

    data class DismissDialog(
        override val tileId: String? = null,
    ) : ScreenTileBroadcastData

    data class DisplaySnackbar(
        override val tileId: String? = null,
        val message: String,
        val duration: SnackbarDuration = SnackbarDuration.Short,
        val actionLabel: String? = null,
        val onAction: (() -> Unit)? = null,
        val onDismiss: (() -> Unit)? = null
    ) : ScreenTileBroadcastData

    data class DismissSnackbar(
        override val tileId: String? = null,
    ) : ScreenTileBroadcastData

    data class DisplayNavigationDrawer(
        override val tileId: String? = null,
    ) : ScreenTileBroadcastData

    data class DismissNavigationDrawer(
        override val tileId: String? = null,
    ) : ScreenTileBroadcastData
}