package dev.catbit.mosaic.server.builder.event.builders.theme

import dev.catbit.mosaic.core.data.schemas.event.events.theme.SetThemeEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.theme.SetThemeEventSchema.ColorScheme
import dev.catbit.mosaic.core.data.schemas.event.events.theme.SetThemeEventSchema.ColorsScheme
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class SetThemeEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val colorsScheme: ColorsScheme
) : EventSchemaBuilder<SetThemeEventSchema>() {

    override fun build() = SetThemeEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        colorsScheme = colorsScheme
    )
}

fun EventSchemaBuilderScope.SetTheme(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    colorsScheme: ColorsScheme
) {
    addBuilder(
        SetThemeEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            colorsScheme = colorsScheme
        )
    )
}

fun colorsScheme(
    lightColorScheme: ColorScheme,
    darkColorScheme: ColorScheme,
) = ColorsScheme(
    lightColorScheme = lightColorScheme,
    darkColorScheme = darkColorScheme
)

fun colorScheme(
    primary: String,
    onPrimary: String,
    primaryContainer: String,
    onPrimaryContainer: String,
    inversePrimary: String,
    secondary: String,
    onSecondary: String,
    secondaryContainer: String,
    onSecondaryContainer: String,
    tertiary: String,
    onTertiary: String,
    tertiaryContainer: String,
    onTertiaryContainer: String,
    background: String,
    onBackground: String,
    surface: String,
    onSurface: String,
    surfaceVariant: String,
    onSurfaceVariant: String,
    surfaceTint: String,
    inverseSurface: String,
    inverseOnSurface: String,
    error: String,
    onError: String,
    errorContainer: String,
    onErrorContainer: String,
    outline: String,
    outlineVariant: String,
    scrim: String,
    surfaceBright: String,
    surfaceDim: String,
    surfaceContainer: String,
    surfaceContainerHigh: String,
    surfaceContainerHighest: String,
    surfaceContainerLow: String,
    surfaceContainerLowest: String,
    primaryFixed: String,
    primaryFixedDim: String,
    onPrimaryFixed: String,
    onPrimaryFixedVariant: String,
    secondaryFixed: String,
    secondaryFixedDim: String,
    onSecondaryFixed: String,
    onSecondaryFixedVariant: String,
    tertiaryFixed: String,
    tertiaryFixedDim: String,
    onTertiaryFixed: String,
    onTertiaryFixedVariant: String,
) = ColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    inversePrimary = inversePrimary,
    secondary = secondary,
    onSecondary = onSecondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    tertiary = tertiary,
    onTertiary = onTertiary,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariant,
    surfaceTint = surfaceTint,
    inverseSurface = inverseSurface,
    inverseOnSurface = inverseOnSurface,
    error = error,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    outline = outline,
    outlineVariant = outlineVariant,
    scrim = scrim,
    surfaceBright = surfaceBright,
    surfaceDim = surfaceDim,
    surfaceContainer = surfaceContainer,
    surfaceContainerHigh = surfaceContainerHigh,
    surfaceContainerHighest = surfaceContainerHighest,
    surfaceContainerLow = surfaceContainerLow,
    surfaceContainerLowest = surfaceContainerLowest,
    primaryFixed = primaryFixed,
    primaryFixedDim = primaryFixedDim,
    onPrimaryFixed = onPrimaryFixed,
    onPrimaryFixedVariant = onPrimaryFixedVariant,
    secondaryFixed = secondaryFixed,
    secondaryFixedDim = secondaryFixedDim,
    onSecondaryFixed = onSecondaryFixed,
    onSecondaryFixedVariant = onSecondaryFixedVariant,
    tertiaryFixed = tertiaryFixed,
    tertiaryFixedDim = tertiaryFixedDim,
    onTertiaryFixed = onTertiaryFixed,
    onTertiaryFixedVariant = onTertiaryFixedVariant
)