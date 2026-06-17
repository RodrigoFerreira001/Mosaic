package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import androidx.compose.material3.SnackbarDuration
import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastData

sealed interface ScreenTileScreenTilesBroadcastData : ScreenTilesBroadcastData {
    data class OnDisplayBottomSheetRequested(
        override val tileId: String? = null,
        val isCancellable: Boolean,
        val fill: Boolean,
    ) : ScreenTileScreenTilesBroadcastData

    data class DismissBottomSheet(
        override val tileId: String? = null,
    ) : ScreenTileScreenTilesBroadcastData

    data class OnDisplayDialogRequested(
        override val tileId: String? = null,
        val isCancellable: Boolean,
        val usePlatformDefaultWidth: Boolean
    ) : ScreenTileScreenTilesBroadcastData

    data class DismissDialog(
        override val tileId: String? = null,
    ) : ScreenTileScreenTilesBroadcastData

    data class DisplaySnackbar(
        override val tileId: String? = null,
        val message: String,
        val duration: SnackbarDuration = SnackbarDuration.Short,
        val actionLabel: String? = null,
        val onAction: (suspend () -> Unit)? = null,
        val onDismiss: (suspend () -> Unit)? = null
    ) : ScreenTileScreenTilesBroadcastData

    data class DismissSnackbar(
        override val tileId: String? = null,
    ) : ScreenTileScreenTilesBroadcastData

    data class DisplayNavigationDrawer(
        override val tileId: String? = null,
    ) : ScreenTileScreenTilesBroadcastData

    data class DismissNavigationDrawer(
        override val tileId: String? = null,
    ) : ScreenTileScreenTilesBroadcastData
}