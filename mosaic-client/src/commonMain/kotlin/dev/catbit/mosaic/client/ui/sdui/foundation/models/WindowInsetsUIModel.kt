package dev.catbit.mosaic.client.ui.sdui.foundation.models

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.waterfall
import androidx.compose.runtime.Composable

sealed interface WindowInsetsUIModel {
    data object SystemBars : WindowInsetsUIModel
    data object CaptionBar : WindowInsetsUIModel
    data object StatusBar : WindowInsetsUIModel
    data object NavigationBar : WindowInsetsUIModel
    data object Ime : WindowInsetsUIModel
    data object DisplayCutout : WindowInsetsUIModel
    data object Waterfall : WindowInsetsUIModel

    @Composable
    fun toComposeWindowInsets() = when (this) {
        CaptionBar -> WindowInsets.captionBar
        DisplayCutout -> WindowInsets.displayCutout
        Ime -> WindowInsets.ime
        NavigationBar -> WindowInsets.navigationBars
        StatusBar -> WindowInsets.statusBars
        SystemBars -> WindowInsets.systemBars
        Waterfall -> WindowInsets.waterfall
    }
}