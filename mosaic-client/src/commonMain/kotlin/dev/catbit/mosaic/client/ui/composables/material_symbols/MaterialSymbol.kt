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

/**
 * Renders one Google Material Symbol glyph as a single-character [Text], using the variable font
 * loaded via [LocalMaterialSymbolFonts] ([MaterialSymbolFonts.loadMaterialSymbolFonts] must have run
 * and provided that `CompositionLocal` — `MosaicApplication`/`MosaicTheme` does this automatically).
 * The bottom-level primitive behind `Icon` — reach for that instead unless working with an
 * `IconSchema`-independent icon name directly.
 *
 * @param iconName Material Symbol name (Google's icon font naming, e.g. `"settings"`).
 * @param modifier applied to the rendered glyph.
 * @param filled whether to use the filled font variant, vs. the outline weight.
 * @param style which of the 3 font families (outlined/rounded/sharp) to draw from. Outlined when
 * `null`.
 * @param size glyph size. Falls back to the ambient `LocalTextStyle`'s font size when `null`.
 * @param tint glyph color. Falls back to `LocalContentColor` when `null`.
 * @param contentDescription accessibility description; when non-null, marks this glyph with the
 * `Role.Image` semantics role.
 */
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

/** Which of the 3 bundled Material Symbols font families a glyph draws from — matches
 * [IconSchema.Style] one-to-one (see `IconSchema.Style.toMaterialSymbolStyle()`). */
enum class MaterialSymbolStyle {
    OUTLINED, ROUNDED, SHARP
}