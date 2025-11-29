package dev.catbit.mosaic.client.material_symbols

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

internal val LocalMaterialSymbolFonts = staticCompositionLocalOf<MaterialSymbolFonts> {
    error("MaterialSymbolFonts not provided")
}

@Stable
internal data class MaterialSymbolFonts(
    val outlined: FontFamily,
    val rounded: FontFamily,
    val sharp: FontFamily
)

@Stable
data class MaterialSymbolFontsConfig(
    val filled: Boolean = false,
    val weight: FontWeight = FontWeight.Normal,
    val grade: Int = 0,
    val opticalSize: TextUnit = 24.sp
)