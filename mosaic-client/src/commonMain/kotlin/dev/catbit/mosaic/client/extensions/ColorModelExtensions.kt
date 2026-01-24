package dev.catbit.mosaic.client.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.catbit.mosaic.core.data.color.ColorModel

@Composable
fun ColorModel.toComposeColor(): Color = when (this) {
    is ColorModel.Hex -> {
        val hex = value.removePrefix("#")
        val color = if (hex.length == 6) "FF$hex" else hex
        try {
            Color(color.toLong(16))
        } catch (_: Exception) {
            Color.Unspecified
        }
    }

    is ColorModel.Rgba -> Color(r, g, b, alpha)
    is ColorModel.Theme -> when (value) {
        ColorModel.Theme.Color.PRIMARY -> MaterialTheme.colorScheme.primary
        ColorModel.Theme.Color.ON_PRIMARY -> MaterialTheme.colorScheme.onPrimary
        ColorModel.Theme.Color.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
        ColorModel.Theme.Color.ON_PRIMARY_CONTAINER -> MaterialTheme.colorScheme.onPrimaryContainer
        ColorModel.Theme.Color.SECONDARY -> MaterialTheme.colorScheme.secondary
        ColorModel.Theme.Color.ON_SECONDARY -> MaterialTheme.colorScheme.onSecondary
        ColorModel.Theme.Color.SECONDARY_CONTAINER -> MaterialTheme.colorScheme.secondaryContainer
        ColorModel.Theme.Color.ON_SECONDARY_CONTAINER -> MaterialTheme.colorScheme.onSecondaryContainer
        ColorModel.Theme.Color.TERTIARY -> MaterialTheme.colorScheme.tertiary
        ColorModel.Theme.Color.ON_TERTIARY -> MaterialTheme.colorScheme.onTertiary
        ColorModel.Theme.Color.TERTIARY_CONTAINER -> MaterialTheme.colorScheme.tertiaryContainer
        ColorModel.Theme.Color.ON_TERTIARY_CONTAINER -> MaterialTheme.colorScheme.onTertiaryContainer
        ColorModel.Theme.Color.ERROR -> MaterialTheme.colorScheme.error
        ColorModel.Theme.Color.ON_ERROR -> MaterialTheme.colorScheme.onError
        ColorModel.Theme.Color.ERROR_CONTAINER -> MaterialTheme.colorScheme.errorContainer
        ColorModel.Theme.Color.ON_ERROR_CONTAINER -> MaterialTheme.colorScheme.onErrorContainer
        ColorModel.Theme.Color.BACKGROUND -> MaterialTheme.colorScheme.background
        ColorModel.Theme.Color.ON_BACKGROUND -> MaterialTheme.colorScheme.onBackground
        ColorModel.Theme.Color.SURFACE -> MaterialTheme.colorScheme.surface
        ColorModel.Theme.Color.ON_SURFACE -> MaterialTheme.colorScheme.onSurface
        ColorModel.Theme.Color.SURFACE_VARIANT -> MaterialTheme.colorScheme.surfaceVariant
        ColorModel.Theme.Color.ON_SURFACE_VARIANT -> MaterialTheme.colorScheme.onSurfaceVariant
        ColorModel.Theme.Color.OUTLINE -> MaterialTheme.colorScheme.outline
        ColorModel.Theme.Color.OUTLINE_VARIANT -> MaterialTheme.colorScheme.outlineVariant
        ColorModel.Theme.Color.SCRIM -> MaterialTheme.colorScheme.scrim
        ColorModel.Theme.Color.INVERSE_SURFACE -> MaterialTheme.colorScheme.inverseSurface
        ColorModel.Theme.Color.INVERSE_ON_SURFACE -> MaterialTheme.colorScheme.inverseOnSurface
        ColorModel.Theme.Color.INVERSE_PRIMARY -> MaterialTheme.colorScheme.inversePrimary
        ColorModel.Theme.Color.SURFACE_DIM -> MaterialTheme.colorScheme.surfaceDim
        ColorModel.Theme.Color.SURFACE_BRIGHT -> MaterialTheme.colorScheme.surfaceBright
        ColorModel.Theme.Color.SURFACE_CONTAINER_LOWEST -> MaterialTheme.colorScheme.surfaceContainerLowest
        ColorModel.Theme.Color.SURFACE_CONTAINER_LOW -> MaterialTheme.colorScheme.surfaceContainerLow
        ColorModel.Theme.Color.SURFACE_CONTAINER -> MaterialTheme.colorScheme.surfaceContainer
        ColorModel.Theme.Color.SURFACE_CONTAINER_HIGH -> MaterialTheme.colorScheme.surfaceContainerHigh
        ColorModel.Theme.Color.SURFACE_CONTAINER_HIGHEST -> MaterialTheme.colorScheme.surfaceContainerHighest
    }
}