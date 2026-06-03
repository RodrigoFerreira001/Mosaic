package dev.catbit.mosaic.core.data.schemas.color

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Polymorphic color value used throughout Mosaic schemas (backgrounds, borders, icon tint, text color, etc.).
 *
 * **Variants:**
 * - [Hex] — CSS-style hex string. Supports 6-character `#RRGGBB` and 8-character `#AARRGGBB`
 *   formats. The `#` prefix is stripped before parsing; invalid strings resolve silently to
 *   `Color.Unspecified` without throwing.
 * - [Rgba] — Float components each in the `0f..1f` range. Maps directly to `Color(r, g, b, alpha)`.
 * - [Theme] — References a Material 3 color role from `MaterialTheme.colorScheme`. Adapts
 *   automatically to light/dark theme. Prefer this variant for all non-brand-specific UI.
 *
 * **DSL helpers (mosaic-server):**
 * ```kotlin
 * color(themeColorPrimary())                  // Theme variant — preferred
 * color(hex = "#FF5722")                      // Hex variant (6-char)
 * color(r = 1f, g = 0f, b = 0f, alpha = 1f)  // Rgba variant
 * ColorSchema.Hex("#FFAABBCC")                // Direct construction also valid
 * ```
 *
 * **Theme color helpers:** `themeColorPrimary()`, `themeColorOnPrimary()`, `themeColorPrimaryContainer()`,
 * `themeColorOnPrimaryContainer()`, `themeColorSecondary()`, `themeColorOnSecondary()`,
 * `themeColorSecondaryContainer()`, `themeColorOnSecondaryContainer()`, `themeColorTertiary()`,
 * `themeColorOnTertiary()`, `themeColorTertiaryContainer()`, `themeColorOnTertiaryContainer()`,
 * `themeColorError()`, `themeColorOnError()`, `themeColorErrorContainer()`, `themeColorOnErrorContainer()`,
 * `themeColorBackground()`, `themeColorOnBackground()`, `themeColorSurface()`, `themeColorOnSurface()`,
 * `themeColorSurfaceVariant()`, `themeColorOnSurfaceVariant()`, `themeColorOutline()`,
 * `themeColorOutlineVariant()`, `themeColorScrim()`, `themeColorInverseSurface()`,
 * `themeColorInverseOnSurface()`, `themeColorInversePrimary()`, `themeColorSurfaceDim()`,
 * `themeColorSurfaceBright()`, `themeColorSurfaceContainerLowest()`, `themeColorSurfaceContainerLow()`,
 * `themeColorSurfaceContainer()`, `themeColorSurfaceContainerHigh()`, `themeColorSurfaceContainerHighest()`.
 */
@Serializable
sealed interface ColorSchema {

    // TODO GRADIENT, BLUR?

    @Serializable
    @SerialName("hex")
    data class Hex(
        @SerialName("value") val value: String
    ) : ColorSchema

    @Serializable
    @SerialName("rgba")
    data class Rgba(
        @SerialName("r") val r: Float,
        @SerialName("g") val g: Float,
        @SerialName("b") val b: Float,
        @SerialName("alpha") val alpha: Float
    ) : ColorSchema

    @Serializable
    @SerialName("theme")
    data class Theme(
        @SerialName("value") val value: Color
    ) : ColorSchema {

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
}