package dev.catbit.mosaic.client.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.catbit.mosaic.core.data.schemas.color.ColorSchema

@Composable
fun ColorSchema.toComposeColor(): Color = when (this) {
    is ColorSchema.Hex -> {
        val hex = value.removePrefix("#")
        val color = if (hex.length == 6) "FF$hex" else hex
        try {
            Color(color.toLong(16))
        } catch (_: Exception) {
            Color.Unspecified
        }
    }

    is ColorSchema.Rgba -> Color(r, g, b, alpha)
    is ColorSchema.Theme -> when (value) {
        ColorSchema.Theme.Color.PRIMARY -> MaterialTheme.colorScheme.primary
        ColorSchema.Theme.Color.ON_PRIMARY -> MaterialTheme.colorScheme.onPrimary
        ColorSchema.Theme.Color.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
        ColorSchema.Theme.Color.ON_PRIMARY_CONTAINER -> MaterialTheme.colorScheme.onPrimaryContainer
        ColorSchema.Theme.Color.SECONDARY -> MaterialTheme.colorScheme.secondary
        ColorSchema.Theme.Color.ON_SECONDARY -> MaterialTheme.colorScheme.onSecondary
        ColorSchema.Theme.Color.SECONDARY_CONTAINER -> MaterialTheme.colorScheme.secondaryContainer
        ColorSchema.Theme.Color.ON_SECONDARY_CONTAINER -> MaterialTheme.colorScheme.onSecondaryContainer
        ColorSchema.Theme.Color.TERTIARY -> MaterialTheme.colorScheme.tertiary
        ColorSchema.Theme.Color.ON_TERTIARY -> MaterialTheme.colorScheme.onTertiary
        ColorSchema.Theme.Color.TERTIARY_CONTAINER -> MaterialTheme.colorScheme.tertiaryContainer
        ColorSchema.Theme.Color.ON_TERTIARY_CONTAINER -> MaterialTheme.colorScheme.onTertiaryContainer
        ColorSchema.Theme.Color.ERROR -> MaterialTheme.colorScheme.error
        ColorSchema.Theme.Color.ON_ERROR -> MaterialTheme.colorScheme.onError
        ColorSchema.Theme.Color.ERROR_CONTAINER -> MaterialTheme.colorScheme.errorContainer
        ColorSchema.Theme.Color.ON_ERROR_CONTAINER -> MaterialTheme.colorScheme.onErrorContainer
        ColorSchema.Theme.Color.BACKGROUND -> MaterialTheme.colorScheme.background
        ColorSchema.Theme.Color.ON_BACKGROUND -> MaterialTheme.colorScheme.onBackground
        ColorSchema.Theme.Color.SURFACE -> MaterialTheme.colorScheme.surface
        ColorSchema.Theme.Color.ON_SURFACE -> MaterialTheme.colorScheme.onSurface
        ColorSchema.Theme.Color.SURFACE_VARIANT -> MaterialTheme.colorScheme.surfaceVariant
        ColorSchema.Theme.Color.ON_SURFACE_VARIANT -> MaterialTheme.colorScheme.onSurfaceVariant
        ColorSchema.Theme.Color.OUTLINE -> MaterialTheme.colorScheme.outline
        ColorSchema.Theme.Color.OUTLINE_VARIANT -> MaterialTheme.colorScheme.outlineVariant
        ColorSchema.Theme.Color.SCRIM -> MaterialTheme.colorScheme.scrim
        ColorSchema.Theme.Color.INVERSE_SURFACE -> MaterialTheme.colorScheme.inverseSurface
        ColorSchema.Theme.Color.INVERSE_ON_SURFACE -> MaterialTheme.colorScheme.inverseOnSurface
        ColorSchema.Theme.Color.INVERSE_PRIMARY -> MaterialTheme.colorScheme.inversePrimary
        ColorSchema.Theme.Color.SURFACE_DIM -> MaterialTheme.colorScheme.surfaceDim
        ColorSchema.Theme.Color.SURFACE_BRIGHT -> MaterialTheme.colorScheme.surfaceBright
        ColorSchema.Theme.Color.SURFACE_CONTAINER_LOWEST -> MaterialTheme.colorScheme.surfaceContainerLowest
        ColorSchema.Theme.Color.SURFACE_CONTAINER_LOW -> MaterialTheme.colorScheme.surfaceContainerLow
        ColorSchema.Theme.Color.SURFACE_CONTAINER -> MaterialTheme.colorScheme.surfaceContainer
        ColorSchema.Theme.Color.SURFACE_CONTAINER_HIGH -> MaterialTheme.colorScheme.surfaceContainerHigh
        ColorSchema.Theme.Color.SURFACE_CONTAINER_HIGHEST -> MaterialTheme.colorScheme.surfaceContainerHighest
    }
}