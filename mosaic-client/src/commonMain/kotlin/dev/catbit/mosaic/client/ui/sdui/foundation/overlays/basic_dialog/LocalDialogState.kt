package dev.catbit.mosaic.client.ui.sdui.foundation.overlays.basic_dialog

import androidx.compose.runtime.compositionLocalOf

val LocalDialogState = compositionLocalOf<DialogState> {
    error("BasicDialogState was not provided")
}