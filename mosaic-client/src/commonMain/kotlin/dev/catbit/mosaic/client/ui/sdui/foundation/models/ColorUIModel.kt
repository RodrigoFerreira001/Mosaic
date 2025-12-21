package dev.catbit.mosaic.client.ui.sdui.foundation.models

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

sealed interface ColorUIModel {

    data class Hex(
        val value: String
    ) : ColorUIModel

    data class Rgba(
        val r: Float,
        val g: Float,
        val b: Float,
        val alpha: Float
    ) : ColorUIModel

    data class Theme(
        val value: Color
    ) : ColorUIModel {

        enum class Color {
            PRIMARY,
            ON_PRIMARY,
            PRIMARY_CONTAINER,
            ON_PRIMARY_CONTAINER,
            SECONDARY,
            ON_SECONDARY,
            SECONDARY_CONTAINER,
            ON_SECONDARY_CONTAINER,
            TERTIARY,
            ON_TERTIARY,
            TERTIARY_CONTAINER,
            ON_TERTIARY_CONTAINER,
            ERROR,
            ON_ERROR,
            ERROR_CONTAINER,
            ON_ERROR_CONTAINER,
            BACKGROUND,
            ON_BACKGROUND,
            SURFACE,
            ON_SURFACE,
            SURFACE_VARIANT,
            ON_SURFACE_VARIANT,
            OUTLINE,
            OUTLINE_VARIANT,
            SCRIM,
            INVERSE_SURFACE,
            INVERSE_ON_SURFACE,
            INVERSE_PRIMARY,
            SURFACE_DIM,
            SURFACE_BRIGHT,
            SURFACE_CONTAINER_LOWEST,
            SURFACE_CONTAINER_LOW,
            SURFACE_CONTAINER,
            SURFACE_CONTAINER_HIGH,
            SURFACE_CONTAINER_HIGHEST
        }
    }

    @Composable
    fun toComposeColor(): Color = when (this) {
        is Hex -> {
            val hex = value.removePrefix("#")
            val color = if (hex.length == 6) "FF$hex" else hex
            try {
                Color(color.toLong(16))
            } catch (_: Exception) {
                Color.Unspecified
            }
        }

        is Rgba -> Color(r, g, b, alpha)
        is Theme -> when (value) {
            Theme.Color.PRIMARY -> MaterialTheme.colorScheme.primary
            Theme.Color.ON_PRIMARY -> MaterialTheme.colorScheme.onPrimary
            Theme.Color.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
            Theme.Color.ON_PRIMARY_CONTAINER -> MaterialTheme.colorScheme.onPrimaryContainer
            Theme.Color.SECONDARY -> MaterialTheme.colorScheme.secondary
            Theme.Color.ON_SECONDARY -> MaterialTheme.colorScheme.onSecondary
            Theme.Color.SECONDARY_CONTAINER -> MaterialTheme.colorScheme.secondaryContainer
            Theme.Color.ON_SECONDARY_CONTAINER -> MaterialTheme.colorScheme.onSecondaryContainer
            Theme.Color.TERTIARY -> MaterialTheme.colorScheme.tertiary
            Theme.Color.ON_TERTIARY -> MaterialTheme.colorScheme.onTertiary
            Theme.Color.TERTIARY_CONTAINER -> MaterialTheme.colorScheme.tertiaryContainer
            Theme.Color.ON_TERTIARY_CONTAINER -> MaterialTheme.colorScheme.onTertiaryContainer
            Theme.Color.ERROR -> MaterialTheme.colorScheme.error
            Theme.Color.ON_ERROR -> MaterialTheme.colorScheme.onError
            Theme.Color.ERROR_CONTAINER -> MaterialTheme.colorScheme.errorContainer
            Theme.Color.ON_ERROR_CONTAINER -> MaterialTheme.colorScheme.onErrorContainer
            Theme.Color.BACKGROUND -> MaterialTheme.colorScheme.background
            Theme.Color.ON_BACKGROUND -> MaterialTheme.colorScheme.onBackground
            Theme.Color.SURFACE -> MaterialTheme.colorScheme.surface
            Theme.Color.ON_SURFACE -> MaterialTheme.colorScheme.onSurface
            Theme.Color.SURFACE_VARIANT -> MaterialTheme.colorScheme.surfaceVariant
            Theme.Color.ON_SURFACE_VARIANT -> MaterialTheme.colorScheme.onSurfaceVariant
            Theme.Color.OUTLINE -> MaterialTheme.colorScheme.outline
            Theme.Color.OUTLINE_VARIANT -> MaterialTheme.colorScheme.outlineVariant
            Theme.Color.SCRIM -> MaterialTheme.colorScheme.scrim
            Theme.Color.INVERSE_SURFACE -> MaterialTheme.colorScheme.inverseSurface
            Theme.Color.INVERSE_ON_SURFACE -> MaterialTheme.colorScheme.inverseOnSurface
            Theme.Color.INVERSE_PRIMARY -> MaterialTheme.colorScheme.inversePrimary
            Theme.Color.SURFACE_DIM -> MaterialTheme.colorScheme.surfaceDim
            Theme.Color.SURFACE_BRIGHT -> MaterialTheme.colorScheme.surfaceBright
            Theme.Color.SURFACE_CONTAINER_LOWEST -> MaterialTheme.colorScheme.surfaceContainerLowest
            Theme.Color.SURFACE_CONTAINER_LOW -> MaterialTheme.colorScheme.surfaceContainerLow
            Theme.Color.SURFACE_CONTAINER -> MaterialTheme.colorScheme.surfaceContainer
            Theme.Color.SURFACE_CONTAINER_HIGH -> MaterialTheme.colorScheme.surfaceContainerHigh
            Theme.Color.SURFACE_CONTAINER_HIGHEST -> MaterialTheme.colorScheme.surfaceContainerHighest
        }
    }
}
