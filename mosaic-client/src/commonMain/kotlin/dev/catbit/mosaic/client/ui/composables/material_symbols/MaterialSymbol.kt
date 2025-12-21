package dev.catbit.mosaic.client.ui.composables.material_symbols

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MaterialSymbol(
    iconName: String,
    style: MaterialSymbolStyle = MaterialSymbolStyle.OUTLINED,
    size: Dp = 24.dp,
    tint: Color = Color.Unspecified
) {
    val materialSymbolFonts = LocalMaterialSymbolFonts.current

    val fontFamily = remember(style) {
        when (style) {
            MaterialSymbolStyle.OUTLINED -> materialSymbolFonts.outlined
            MaterialSymbolStyle.ROUNDED -> materialSymbolFonts.rounded
            MaterialSymbolStyle.SHARP -> materialSymbolFonts.sharp
        }
    }

    val density = LocalDensity.current
    val fontSize = with(density) { size.toSp() }

    Text(
        text = iconName,
        fontFamily = fontFamily,
        fontSize = fontSize,
        lineHeight = fontSize,
        color = tint
    )
}

enum class MaterialSymbolStyle {
    OUTLINED, ROUNDED, SHARP
}