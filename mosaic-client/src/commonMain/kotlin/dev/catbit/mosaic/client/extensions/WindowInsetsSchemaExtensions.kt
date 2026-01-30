package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import dev.catbit.mosaic.core.data.schemas.tile.style.WindowInsetsSchema

@Composable
fun WindowInsetsSchema.toComposeWindowInsets() = when (this) {
    WindowInsetsSchema.CaptionBar -> WindowInsets.captionBar
    WindowInsetsSchema.DisplayCutout -> WindowInsets.displayCutout
    WindowInsetsSchema.Ime -> WindowInsets.ime
    WindowInsetsSchema.NavigationBar -> WindowInsets.navigationBars
    WindowInsetsSchema.StatusBar -> WindowInsets.statusBars
    WindowInsetsSchema.SystemBars -> WindowInsets.systemBars
    WindowInsetsSchema.Waterfall -> WindowInsets.waterfall
}