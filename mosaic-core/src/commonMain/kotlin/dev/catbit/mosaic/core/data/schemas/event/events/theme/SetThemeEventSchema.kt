package dev.catbit.mosaic.core.data.schemas.event.events.theme

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Overrides the app's Material color schemes at runtime with [colorsScheme], which carries a full
 * light and a full dark scheme — every Material 3 role, from `primary` through the surface
 * container and fixed-accent families. Both are applied at once, so the app keeps following the
 * system's light/dark setting.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — always, after the schemes were applied. No data is passed
 *   downstream.
 */
@Immutable
@Triggers([OnSuccessEventTrigger::class])
@Serializable
@SerialName("SetTheme")
data class SetThemeEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("colorsScheme") val colorsScheme: ColorsScheme
) : EventSchema {

    @Serializable
    data class ColorsScheme(
        @SerialName("lightColorScheme") val lightColorScheme: ColorScheme,
        @SerialName("darkColorScheme") val darkColorScheme: ColorScheme,
    )

    @Serializable
    data class ColorScheme(
        @SerialName("primary") val primary: String,
        @SerialName("onPrimary") val onPrimary: String,
        @SerialName("primaryContainer") val primaryContainer: String,
        @SerialName("onPrimaryContainer") val onPrimaryContainer: String,
        @SerialName("inversePrimary") val inversePrimary: String,
        @SerialName("secondary") val secondary: String,
        @SerialName("onSecondary") val onSecondary: String,
        @SerialName("secondaryContainer") val secondaryContainer: String,
        @SerialName("onSecondaryContainer") val onSecondaryContainer: String,
        @SerialName("tertiary") val tertiary: String,
        @SerialName("onTertiary") val onTertiary: String,
        @SerialName("tertiaryContainer") val tertiaryContainer: String,
        @SerialName("onTertiaryContainer") val onTertiaryContainer: String,
        @SerialName("background") val background: String,
        @SerialName("onBackground") val onBackground: String,
        @SerialName("surface") val surface: String,
        @SerialName("onSurface") val onSurface: String,
        @SerialName("surfaceVariant") val surfaceVariant: String,
        @SerialName("onSurfaceVariant") val onSurfaceVariant: String,
        @SerialName("surfaceTint") val surfaceTint: String,
        @SerialName("inverseSurface") val inverseSurface: String,
        @SerialName("inverseOnSurface") val inverseOnSurface: String,
        @SerialName("error") val error: String,
        @SerialName("onError") val onError: String,
        @SerialName("errorContainer") val errorContainer: String,
        @SerialName("onErrorContainer") val onErrorContainer: String,
        @SerialName("outline") val outline: String,
        @SerialName("outlineVariant") val outlineVariant: String,
        @SerialName("scrim") val scrim: String,
        @SerialName("surfaceBright") val surfaceBright: String,
        @SerialName("surfaceDim") val surfaceDim: String,
        @SerialName("surfaceContainer") val surfaceContainer: String,
        @SerialName("surfaceContainerHigh") val surfaceContainerHigh: String,
        @SerialName("surfaceContainerHighest") val surfaceContainerHighest: String,
        @SerialName("surfaceContainerLow") val surfaceContainerLow: String,
        @SerialName("surfaceContainerLowest") val surfaceContainerLowest: String,
        @SerialName("primaryFixed") val primaryFixed: String,
        @SerialName("primaryFixedDim") val primaryFixedDim: String,
        @SerialName("onPrimaryFixed") val onPrimaryFixed: String,
        @SerialName("onPrimaryFixedVariant") val onPrimaryFixedVariant: String,
        @SerialName("secondaryFixed") val secondaryFixed: String,
        @SerialName("secondaryFixedDim") val secondaryFixedDim: String,
        @SerialName("onSecondaryFixed") val onSecondaryFixed: String,
        @SerialName("onSecondaryFixedVariant") val onSecondaryFixedVariant: String,
        @SerialName("tertiaryFixed") val tertiaryFixed: String,
        @SerialName("tertiaryFixedDim") val tertiaryFixedDim: String,
        @SerialName("onTertiaryFixed") val onTertiaryFixed: String,
        @SerialName("onTertiaryFixedVariant") val onTertiaryFixedVariant: String
    )
}