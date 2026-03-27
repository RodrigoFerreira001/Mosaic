package dev.catbit.mosaic.client.ui.composables.material_symbols

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.catbit.mosaic.client.generated.resources.Res
import dev.catbit.mosaic.client.generated.resources.materialSymbolsOutlined
import dev.catbit.mosaic.client.generated.resources.materialSymbolsRounded
import dev.catbit.mosaic.client.generated.resources.materialSymbolsSharp
import org.jetbrains.compose.resources.Font

internal val LocalMaterialSymbolFonts = staticCompositionLocalOf<MaterialSymbolFonts> {
    error("MaterialSymbolFonts not provided")
}

@Stable
data class MaterialSymbolFonts(
    val outlined: FontFamily,
    val outlinedFilled: FontFamily,
    val rounded: FontFamily,
    val roundedFilled: FontFamily,
    val sharp: FontFamily,
    val sharpFilled: FontFamily
)

@Stable
data class MaterialSymbolFontsConfig(
    val weight: FontWeight = FontWeight.Normal,
    val grade: Int = 0,
    val opticalSize: TextUnit = 24.sp
)

@Composable
fun loadMaterialSymbolFonts(
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
        FontVariation.Setting("FILL", 0f),
        FontVariation.grade(grade),
        FontVariation.Setting("opsz", opticalSize.value),
        FontVariation.weight(weight.weight)
    )
    val settingsFilled = FontVariation.Settings(
        FontVariation.Setting("FILL", 1f),
        FontVariation.grade(grade),
        FontVariation.Setting("opsz", opticalSize.value),
        FontVariation.weight(weight.weight)
    )

    MaterialSymbolFonts(
        outlined = FontFamily(
            Font(
                resource = Res.font.materialSymbolsOutlined,
                variationSettings = settings
            )
        ),
        outlinedFilled = FontFamily(
            Font(
                resource = Res.font.materialSymbolsOutlined,
                variationSettings = settingsFilled
            )
        ),
        rounded = FontFamily(
            Font(
                resource = Res.font.materialSymbolsRounded,
                variationSettings = settings
            )
        ),
        roundedFilled = FontFamily(
            Font(
                resource = Res.font.materialSymbolsRounded,
                variationSettings = settingsFilled
            )
        ),
        sharp = FontFamily(
            Font(
                resource = Res.font.materialSymbolsSharp,
                variationSettings = settings
            )
        ),
        sharpFilled = FontFamily(
            Font(
                resource = Res.font.materialSymbolsSharp,
                variationSettings = settingsFilled
            )
        )
    )
}