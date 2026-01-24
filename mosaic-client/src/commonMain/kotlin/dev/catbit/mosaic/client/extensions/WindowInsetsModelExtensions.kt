package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import dev.catbit.mosaic.core.data.tile.style.WindowInsetsModel

@Composable
fun WindowInsetsModel.toComposeWindowInsets() = when (this) {
    WindowInsetsModel.CaptionBar -> WindowInsets.captionBar
    WindowInsetsModel.DisplayCutout -> WindowInsets.displayCutout
    WindowInsetsModel.Ime -> WindowInsets.ime
    WindowInsetsModel.NavigationBar -> WindowInsets.navigationBars
    WindowInsetsModel.StatusBar -> WindowInsets.statusBars
    WindowInsetsModel.SystemBars -> WindowInsets.systemBars
    WindowInsetsModel.Waterfall -> WindowInsets.waterfall
}