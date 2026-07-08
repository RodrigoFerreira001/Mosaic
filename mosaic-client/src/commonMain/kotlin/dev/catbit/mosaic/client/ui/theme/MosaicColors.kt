package dev.catbit.mosaic.client.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

internal class MosaicColors(
    private val defaultLightColorScheme: ColorScheme,
    private val defaultDarkColorScheme: ColorScheme
) {
    var lightColorScheme by mutableStateOf(defaultLightColorScheme)
        private set
    var darkColorScheme by mutableStateOf(defaultDarkColorScheme)
        private set

    fun setColorSchemes(
        lightColorScheme: ColorScheme,
        darkColorScheme: ColorScheme
    ) {
        this.lightColorScheme = lightColorScheme
        this.darkColorScheme = darkColorScheme
    }

    fun resetColorScheme() {
        lightColorScheme = defaultLightColorScheme
        darkColorScheme = defaultDarkColorScheme
    }
}