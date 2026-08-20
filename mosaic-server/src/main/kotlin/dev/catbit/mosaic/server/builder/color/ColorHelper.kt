package dev.catbit.mosaic.server.builder.color

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema

/** A fixed color from a hex string (`"#RRGGBB"` or `"#AARRGGBB"`), independent of the app's theme. */
fun color(
    hex: String
) = ColorSchema.Hex(
    value = hex
)

/**
 * A fixed color from individual RGBA float components (each 0f–1f), independent of the app's
 * theme.
 *
 * @param r Red channel, from 0f to 1f. Defaults to 0f.
 * @param g Green channel, from 0f to 1f. Defaults to 0f.
 * @param b Blue channel, from 0f to 1f. Defaults to 0f.
 * @param alpha Opacity, from 0f to 1f. Defaults to 1f.
 */
fun color(
    r: Float = 0f,
    g: Float = 0f,
    b: Float = 0f,
    alpha: Float = 1f
) = ColorSchema.Rgba(
    r = r,
    g = g,
    b = b,
    alpha = alpha
)

/**
 * A theme-driven color that resolves to [value] from the app's active Material 3 `ColorScheme`
 * at render time — use with one of the `themeColor*` tokens so the tile follows theme and
 * light/dark changes automatically.
 */
fun color(
    value: ColorSchema.Theme.Color
) = ColorSchema.Theme(
    value = value
)

/** Theme's primary color — the highest-emphasis accent, used on prominent components like filled buttons. */
fun themeColorPrimary() = ColorSchema.Theme.Color.PRIMARY

/** Theme's color for content drawn on top of [themeColorPrimary]. */
fun themeColorOnPrimary() = ColorSchema.Theme.Color.ON_PRIMARY

/** Theme's standout container color built from the primary hue, for less prominent primary-tinted surfaces. */
fun themeColorPrimaryContainer() = ColorSchema.Theme.Color.PRIMARY_CONTAINER

/** Theme's color for content drawn on top of [themeColorPrimaryContainer]. */
fun themeColorOnPrimaryContainer() = ColorSchema.Theme.Color.ON_PRIMARY_CONTAINER

/** Theme's secondary color — a less prominent accent, used for filter chips and secondary controls. */
fun themeColorSecondary() = ColorSchema.Theme.Color.SECONDARY

/** Theme's color for content drawn on top of [themeColorSecondary]. */
fun themeColorOnSecondary() = ColorSchema.Theme.Color.ON_SECONDARY

/** Theme's standout container color built from the secondary hue. */
fun themeColorSecondaryContainer() = ColorSchema.Theme.Color.SECONDARY_CONTAINER

/** Theme's color for content drawn on top of [themeColorSecondaryContainer]. */
fun themeColorOnSecondaryContainer() = ColorSchema.Theme.Color.ON_SECONDARY_CONTAINER

/** Theme's tertiary color — a contrasting accent, used to balance primary/secondary or draw attention. */
fun themeColorTertiary() = ColorSchema.Theme.Color.TERTIARY

/** Theme's color for content drawn on top of [themeColorTertiary]. */
fun themeColorOnTertiary() = ColorSchema.Theme.Color.ON_TERTIARY

/** Theme's standout container color built from the tertiary hue. */
fun themeColorTertiaryContainer() = ColorSchema.Theme.Color.TERTIARY_CONTAINER

/** Theme's color for content drawn on top of [themeColorTertiaryContainer]. */
fun themeColorOnTertiaryContainer() = ColorSchema.Theme.Color.ON_TERTIARY_CONTAINER

/** Theme's error color, for validation failures and destructive actions. */
fun themeColorError() = ColorSchema.Theme.Color.ERROR

/** Theme's color for content drawn on top of [themeColorError]. */
fun themeColorOnError() = ColorSchema.Theme.Color.ON_ERROR

/** Theme's standout container color built from the error hue. */
fun themeColorErrorContainer() = ColorSchema.Theme.Color.ERROR_CONTAINER

/** Theme's color for content drawn on top of [themeColorErrorContainer]. */
fun themeColorOnErrorContainer() = ColorSchema.Theme.Color.ON_ERROR_CONTAINER

/** Theme's screen background color, the base layer behind all content. */
fun themeColorBackground() = ColorSchema.Theme.Color.BACKGROUND

/** Theme's color for content drawn on top of [themeColorBackground]. */
fun themeColorOnBackground() = ColorSchema.Theme.Color.ON_BACKGROUND

/** Theme's default surface color for components (cards, sheets, menus). */
fun themeColorSurface() = ColorSchema.Theme.Color.SURFACE

/** Theme's color for content drawn on top of [themeColorSurface]. */
fun themeColorOnSurface() = ColorSchema.Theme.Color.ON_SURFACE

/** Theme's alternate surface color, for visually separating surfaces with lower emphasis. */
fun themeColorSurfaceVariant() = ColorSchema.Theme.Color.SURFACE_VARIANT

/** Theme's color for content drawn on top of [themeColorSurfaceVariant]. */
fun themeColorOnSurfaceVariant() = ColorSchema.Theme.Color.ON_SURFACE_VARIANT

/** Theme's color for borders and dividers that need to be clearly visible. */
fun themeColorOutline() = ColorSchema.Theme.Color.OUTLINE

/** Theme's color for subtler borders and dividers, decorative rather than functional. */
fun themeColorOutlineVariant() = ColorSchema.Theme.Color.OUTLINE_VARIANT

/** Theme's scrim color, applied over content behind modal surfaces (dialogs, sheets). */
fun themeColorScrim() = ColorSchema.Theme.Color.SCRIM

/** Theme's inverted surface color, used for components that flip against the current brightness (e.g. `Snackbar`). */
fun themeColorInverseSurface() = ColorSchema.Theme.Color.INVERSE_SURFACE

/** Theme's color for content drawn on top of [themeColorInverseSurface]. */
fun themeColorInverseOnSurface() = ColorSchema.Theme.Color.INVERSE_ON_SURFACE

/** Theme's primary color adapted for use on top of [themeColorInverseSurface]. */
fun themeColorInversePrimary() = ColorSchema.Theme.Color.INVERSE_PRIMARY

/** Theme's dimmest surface tone, the low end of the surface elevation scale. */
fun themeColorSurfaceDim() = ColorSchema.Theme.Color.SURFACE_DIM

/** Theme's brightest surface tone, the high end of the surface elevation scale. */
fun themeColorSurfaceBright() = ColorSchema.Theme.Color.SURFACE_BRIGHT

/** Theme's lowest-emphasis surface container tone. */
fun themeColorSurfaceContainerLowest() = ColorSchema.Theme.Color.SURFACE_CONTAINER_LOWEST

/** Theme's low-emphasis surface container tone. */
fun themeColorSurfaceContainerLow() = ColorSchema.Theme.Color.SURFACE_CONTAINER_LOW

/** Theme's default surface container tone, for components that sit above the background. */
fun themeColorSurfaceContainer() = ColorSchema.Theme.Color.SURFACE_CONTAINER

/** Theme's high-emphasis surface container tone. */
fun themeColorSurfaceContainerHigh() = ColorSchema.Theme.Color.SURFACE_CONTAINER_HIGH

/** Theme's highest-emphasis surface container tone. */
fun themeColorSurfaceContainerHighest() = ColorSchema.Theme.Color.SURFACE_CONTAINER_HIGHEST