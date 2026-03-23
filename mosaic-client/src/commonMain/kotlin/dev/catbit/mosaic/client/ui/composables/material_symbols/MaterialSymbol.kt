package dev.catbit.mosaic.client.ui.composables.material_symbols

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Dp

@Composable
fun MaterialSymbol(
    iconName: String,
    modifier: Modifier = Modifier,
    style: MaterialSymbolStyle? = null,
    size: Dp? = null,
    tint: Color? = null,
) {
    val materialSymbolFonts = LocalMaterialSymbolFonts.current

    val fontFamily = remember(style) {
        when (style) {
            MaterialSymbolStyle.ROUNDED -> materialSymbolFonts.rounded
            MaterialSymbolStyle.SHARP -> materialSymbolFonts.sharp
            else -> materialSymbolFonts.outlined
        }
    }

    val density = LocalDensity.current
    val localTextStyle = LocalTextStyle.current
    val fontSize = with(density) { size?.toSp() } ?: localTextStyle.fontSize
    val tint = tint ?: LocalContentColor.current

    Text(
        modifier = modifier,
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