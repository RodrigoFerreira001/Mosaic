package dev.catbit.mosaic.client.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import dev.catbit.mosaic.client.material_symbols.LocalMaterialSymbolFonts
import dev.catbit.mosaic.client.material_symbols.MaterialSymbolFonts
import dev.catbit.mosaic.client.material_symbols.MaterialSymbolFontsConfig
import dev.catbit.mosaic_core.mosaic_client.generated.resources.Res
import dev.catbit.mosaic_core.mosaic_client.generated.resources.materialSymbolsOutlined
import dev.catbit.mosaic_core.mosaic_client.generated.resources.materialSymbolsRounded
import dev.catbit.mosaic_core.mosaic_client.generated.resources.materialSymbolsSharp
import org.jetbrains.compose.resources.Font

@Composable
fun MosaicTheme(
    materialSymbolFontsConfig: MaterialSymbolFontsConfig = MaterialSymbolFontsConfig(),
    content: @Composable () -> Unit
) {
    key(materialSymbolFontsConfig) {
        CompositionLocalProvider(
            LocalMaterialSymbolFonts provides materialSymbolFonts(materialSymbolFontsConfig)
        ) {
            content()
        }
    }
}

@Composable
private fun materialSymbolFonts(
    config: MaterialSymbolFontsConfig
) = with(config) {

    require(weight.weight in 100..700) {
        "weight must be in [100, 700]"
    }

    require(grade in -25..200) {
        "grande must be in [-25, 200]"
    }

    require(opticalSize.value in 20f..48f) {
        "opticalSize must be in [20.sp, 48.sp]"
    }

    val settings = FontVariation.Settings(
        FontVariation.Setting("FILL", if (filled) 1f else 0f),
        FontVariation.grade(grade),
        FontVariation.Setting("opsz", opticalSize.value),
        FontVariation.weight(weight.weight),
    )

    MaterialSymbolFonts(
        outlined = FontFamily(
            Font(
                resource = Res.font.materialSymbolsOutlined,
                variationSettings = settings
            )
        ),
        rounded = FontFamily(
            Font(
                resource = Res.font.materialSymbolsRounded,
                variationSettings = settings
            )
        ),
        sharp = FontFamily(
            Font(
                resource = Res.font.materialSymbolsSharp,
                variationSettings = settings
            )
        )
    )
}