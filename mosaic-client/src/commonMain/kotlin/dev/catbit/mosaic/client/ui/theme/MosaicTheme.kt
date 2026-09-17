package dev.catbit.mosaic.client.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import dev.catbit.material_symbols.MaterialSymbolFontsConfig
import dev.catbit.material_symbols.MaterialSymbolsRenderingScope

@Composable
fun MosaicTheme(
    colors: MosaicColors = MosaicColors(
        defaultLightColorScheme = lightColorScheme(),
        defaultDarkColorScheme = darkColorScheme()
    ),
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    materialSymbolFontsConfig: MaterialSymbolFontsConfig = MaterialSymbolFontsConfig(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (isSystemInDarkTheme())
        colors.darkColorScheme
    else
        colors.lightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = typography,
    ) {
        MaterialSymbolsRenderingScope(materialSymbolFontsConfig) {
            content()
        }
    }
}