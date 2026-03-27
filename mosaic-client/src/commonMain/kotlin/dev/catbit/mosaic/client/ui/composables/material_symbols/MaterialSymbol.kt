package dev.catbit.mosaic.client.ui.composables.material_symbols

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import dev.catbit.mosaic.client.ui.modifiers.thenIfNotNull

@Composable
fun MaterialSymbol(
    iconName: String,
    modifier: Modifier = Modifier,
    filled: Boolean = false,
    style: MaterialSymbolStyle? = null,
    size: Dp? = null,
    tint: Color? = null,
    contentDescription: String? = null
) {
    val materialSymbolFonts = LocalMaterialSymbolFonts.current

    val fontFamily = if (filled) {
        when (style) {
            MaterialSymbolStyle.ROUNDED -> materialSymbolFonts.roundedFilled
            MaterialSymbolStyle.SHARP -> materialSymbolFonts.sharpFilled
            else -> materialSymbolFonts.outlinedFilled
        }
    } else {
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
        modifier = modifier.thenIfNotNull(contentDescription) { contentDescription ->
            semantics {
                this.contentDescription = contentDescription
                role = Role.Image
            }
        },
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