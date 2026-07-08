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
 * Overrides the app's Material3 [ColorScheme] at runtime, for both light and dark mode, via
 * [colorsScheme]. At runtime the runner builds two full Compose `ColorScheme` instances (one per
 * [ColorsScheme.lightColorScheme]/[ColorsScheme.darkColorScheme]) from the hex strings and applies
 * them globally, overriding the theme currently in effect regardless of which screen is on
 * display.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — always, once the new color schemes have been applied.
 *
 * **Notes:** Every field in [ColorScheme] is a hex color string (e.g. `"#FF5722"` or `"FF5722"`),
 * parsed via `String.toColor()` — not a `ColorSchema`/`color(...)` builder value. The override
 * persists until [ResetThemeEventSchema] is dispatched or the app restarts; it is not tied to
 * screen navigation. Use the `colorsScheme(...)`/`colorScheme(...)` DSL helper functions
 * (`mosaic-server`) to build [ColorsScheme]/[ColorScheme] instances instead of constructing them
 * directly.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("SetTheme")
data class SetThemeEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("colorsScheme") val colorsScheme: ColorsScheme
) : EventSchema {

    /** Pair of full Material3 color schemes applied by [SetThemeEventSchema]. */
    @Serializable
    data class ColorsScheme(
        @SerialName("lightColorScheme") val lightColorScheme: ColorScheme,
        @SerialName("darkColorScheme") val darkColorScheme: ColorScheme,
    )

    /**
     * Mirrors every field of Compose Material3's `ColorScheme`, one hex color string per role
     * (e.g. `primary`, `onPrimary`, `surfaceContainerHighest`). Applied via `String.toColor()`
     * on the client.
     */
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