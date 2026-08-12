package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import androidx.compose.material3.SnackbarDuration
import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastData

sealed interface ScreenTileScreenTilesBroadcastData : ScreenTilesBroadcastData {

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