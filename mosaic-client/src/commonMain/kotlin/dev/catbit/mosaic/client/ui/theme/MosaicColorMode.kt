package dev.catbit.mosaic.client.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MosaicColorMode {

    var colorMode by mutableStateOf(ColorMode.SYSTEM_DEFAULT)
        private set

    fun setColorMode(mode: ColorMode) {
        colorMode = mode
    }

    enum class ColorMode {
        LIGHT,
        DARK,
        SYSTEM_DEFAULT
    }
}