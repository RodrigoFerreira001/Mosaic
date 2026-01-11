package dev.catbit.mosaic.client.ui.sdui.foundation.overlays.snackbar

import androidx.compose.runtime.compositionLocalOf

val LocalSnackBarState = compositionLocalOf<SnackBarState> {
    error("SnackBarState was not provided")
}