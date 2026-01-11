package dev.catbit.mosaic.client.ui.sdui.foundation.overlays.bottom_sheet

import androidx.compose.runtime.compositionLocalOf

val LocalBottomSheetState = compositionLocalOf<BottomSheetState> {
    error("BottomSheetState was not provided")
}