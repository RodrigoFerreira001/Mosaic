package dev.catbit.mosaic.server.builder.color

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema

fun color(
    hex: String
) = ColorSchema.Hex(
    value = hex
)

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

fun color(
    value: ColorSchema.Theme.Color
) = ColorSchema.Theme(
    value = value
)

fun themeColorPrimary() = ColorSchema.Theme.Color.PRIMARY
fun themeColorOnPrimary() = ColorSchema.Theme.Color.ON_PRIMARY
fun themeColorPrimaryContainer() = ColorSchema.Theme.Color.PRIMARY_CONTAINER
fun themeColorOnPrimaryContainer() = ColorSchema.Theme.Color.ON_PRIMARY_CONTAINER
fun themeColorSecondary() = ColorSchema.Theme.Color.SECONDARY
fun themeColorOnSecondary() = ColorSchema.Theme.Color.ON_SECONDARY
fun themeColorSecondaryContainer() = ColorSchema.Theme.Color.SECONDARY_CONTAINER
fun themeColorOnSecondaryContainer() = ColorSchema.Theme.Color.ON_SECONDARY_CONTAINER
fun themeColorTertiary() = ColorSchema.Theme.Color.TERTIARY
fun themeColorOnTertiary() = ColorSchema.Theme.Color.ON_TERTIARY
fun themeColorTertiaryContainer() = ColorSchema.Theme.Color.TERTIARY_CONTAINER
fun themeColorOnTertiaryContainer() = ColorSchema.Theme.Color.ON_TERTIARY_CONTAINER
fun themeColorError() = ColorSchema.Theme.Color.ERROR
fun themeColorOnError() = ColorSchema.Theme.Color.ON_ERROR
fun themeColorErrorContainer() = ColorSchema.Theme.Color.ERROR_CONTAINER
fun themeColorOnErrorContainer() = ColorSchema.Theme.Color.ON_ERROR_CONTAINER
fun themeColorBackground() = ColorSchema.Theme.Color.BACKGROUND
fun themeColorOnBackground() = ColorSchema.Theme.Color.ON_BACKGROUND
fun themeColorSurface() = ColorSchema.Theme.Color.SURFACE
fun themeColorOnSurface() = ColorSchema.Theme.Color.ON_SURFACE
fun themeColorSurfaceVariant() = ColorSchema.Theme.Color.SURFACE_VARIANT
fun themeColorOnSurfaceVariant() = ColorSchema.Theme.Color.ON_SURFACE_VARIANT
fun themeColorOutline() = ColorSchema.Theme.Color.OUTLINE
fun themeColorOutlineVariant() = ColorSchema.Theme.Color.OUTLINE_VARIANT
fun themeColorScrim() = ColorSchema.Theme.Color.SCRIM
fun themeColorInverseSurface() = ColorSchema.Theme.Color.INVERSE_SURFACE
fun themeColorInverseOnSurface() = ColorSchema.Theme.Color.INVERSE_ON_SURFACE
fun themeColorInversePrimary() = ColorSchema.Theme.Color.INVERSE_PRIMARY
fun themeColorSurfaceDim() = ColorSchema.Theme.Color.SURFACE_DIM
fun themeColorSurfaceBright() = ColorSchema.Theme.Color.SURFACE_BRIGHT
fun themeColorSurfaceContainerLowest() = ColorSchema.Theme.Color.SURFACE_CONTAINER_LOWEST
fun themeColorSurfaceContainerLow() = ColorSchema.Theme.Color.SURFACE_CONTAINER_LOW
fun themeColorSurfaceContainer() = ColorSchema.Theme.Color.SURFACE_CONTAINER
fun themeColorSurfaceContainerHigh() = ColorSchema.Theme.Color.SURFACE_CONTAINER_HIGH
fun themeColorSurfaceContainerHighest() = ColorSchema.Theme.Color.SURFACE_CONTAINER_HIGHEST