package dev.catbit.mosaic.sample.client.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import mosaic.sample_client.shared.generated.resources.Res
import mosaic.sample_client.shared.generated.resources.googleSans
import org.jetbrains.compose.resources.Font

@Composable
fun SampleTypography(): Typography = with(Typography()) {

    val jostFont = Font(Res.font.googleSans)

    val jostFontFamily = FontFamily(
        jostFont
    )

    Typography(
        displayLarge = displayLarge.copy(fontFamily = jostFontFamily),
        displayMedium = displayMedium.copy(fontFamily = jostFontFamily),
        displaySmall = displaySmall.copy(fontFamily = jostFontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = jostFontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = jostFontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = jostFontFamily),
        titleLarge = titleLarge.copy(fontFamily = jostFontFamily),
        titleMedium = titleMedium.copy(fontFamily = jostFontFamily),
        titleSmall = titleSmall.copy(fontFamily = jostFontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = jostFontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = jostFontFamily),
        bodySmall = bodySmall.copy(fontFamily = jostFontFamily),
        labelLarge = labelLarge.copy(fontFamily = jostFontFamily),
        labelMedium = labelMedium.copy(fontFamily = jostFontFamily),
        labelSmall = labelSmall.copy(fontFamily = jostFontFamily),
    )
}