package dev.catbit.mosaic.client.ui.sdui.foundation.local_providers

import androidx.compose.runtime.compositionLocalOf
import dev.catbit.mosaic.client.ui.sdui.foundation.models.SharedHorizontalArea

val LocalSharedHorizontalAreaProvider = compositionLocalOf<SharedHorizontalArea> {
    SharedHorizontalArea.Undefined
}