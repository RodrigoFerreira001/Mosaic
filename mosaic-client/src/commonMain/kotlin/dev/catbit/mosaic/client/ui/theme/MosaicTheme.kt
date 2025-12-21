package dev.catbit.mosaic.client.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import dev.catbit.mosaic.client.ui.composables.material_symbols.LocalMaterialSymbolFonts
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolFontsConfig
import dev.catbit.mosaic.client.ui.composables.material_symbols.loadMaterialSymbolFonts

@Composable
fun MosaicTheme(
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    materialSymbolFontsConfig: MaterialSymbolFontsConfig = MaterialSymbolFontsConfig(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = typography,
    ) {
        key(materialSymbolFontsConfig) {
            CompositionLocalProvider(
                LocalMaterialSymbolFonts provides loadMaterialSymbolFonts(materialSymbolFontsConfig)
            ) {
                content()
            }
        }
    }
}