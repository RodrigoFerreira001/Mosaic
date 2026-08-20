package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.events.theme.SetThemeEventSchema.ColorsScheme
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.theme.SetTheme
import dev.catbit.mosaic.server.builder.event.builders.theme.colorScheme
import dev.catbit.mosaic.server.builder.event.builders.theme.colorsScheme
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button

// Sample scheme ("ocean" tones) for this demo only — not the app's official Material baseline.
private val oceanLightScheme = colorScheme(
    primary = "#006874", onPrimary = "#FFFFFF", primaryContainer = "#9EEFFD", onPrimaryContainer = "#001F24",
    inversePrimary = "#83D2E0",
    secondary = "#4A6267", onSecondary = "#FFFFFF", secondaryContainer = "#CDE7EC", onSecondaryContainer = "#051F23",
    tertiary = "#535D7E", onTertiary = "#FFFFFF", tertiaryContainer = "#DAE1FF", onTertiaryContainer = "#101A37",
    background = "#FAFDFD", onBackground = "#191C1D", surface = "#FAFDFD", onSurface = "#191C1D",
    surfaceVariant = "#DBE4E6", onSurfaceVariant = "#3F484A", surfaceTint = "#006874",
    inverseSurface = "#2E3132", inverseOnSurface = "#EFF1F1",
    error = "#BA1A1A", onError = "#FFFFFF", errorContainer = "#FFDAD6", onErrorContainer = "#410002",
    outline = "#6F797A", outlineVariant = "#BFC8CA", scrim = "#000000",
    surfaceBright = "#FAFDFD", surfaceDim = "#DAD9D9",
    surfaceContainer = "#EEF0F0", surfaceContainerHigh = "#E8EBEB", surfaceContainerHighest = "#E2E5E5",
    surfaceContainerLow = "#F4F6F6", surfaceContainerLowest = "#FFFFFF",
    primaryFixed = "#9EEFFD", primaryFixedDim = "#83D2E0", onPrimaryFixed = "#001F24", onPrimaryFixedVariant = "#004F58",
    secondaryFixed = "#CDE7EC", secondaryFixedDim = "#B1CBD0", onSecondaryFixed = "#051F23", onSecondaryFixedVariant = "#334B4F",
    tertiaryFixed = "#DAE1FF", tertiaryFixedDim = "#BBC6EA", onTertiaryFixed = "#101A37", onTertiaryFixedVariant = "#3C4664",
)

private val oceanDarkScheme = colorScheme(
    primary = "#83D2E0", onPrimary = "#00363D", primaryContainer = "#004F58", onPrimaryContainer = "#9EEFFD",
    inversePrimary = "#006874",
    secondary = "#B1CBD0", onSecondary = "#1C3438", secondaryContainer = "#334B4F", onSecondaryContainer = "#CDE7EC",
    tertiary = "#BBC6EA", onTertiary = "#252F4D", tertiaryContainer = "#3C4664", onTertiaryContainer = "#DAE1FF",
    background = "#191C1D", onBackground = "#E1E3E3", surface = "#191C1D", onSurface = "#E1E3E3",
    surfaceVariant = "#3F484A", onSurfaceVariant = "#BFC8CA", surfaceTint = "#83D2E0",
    inverseSurface = "#E1E3E3", inverseOnSurface = "#191C1D",
    error = "#FFB4AB", onError = "#690005", errorContainer = "#93000A", onErrorContainer = "#FFDAD6",
    outline = "#899294", outlineVariant = "#3F484A", scrim = "#000000",
    surfaceBright = "#3F4344", surfaceDim = "#191C1D",
    surfaceContainer = "#1D2021", surfaceContainerHigh = "#282B2C", surfaceContainerHighest = "#333637",
    surfaceContainerLow = "#191C1D", surfaceContainerLowest = "#0E1415",
    primaryFixed = "#9EEFFD", primaryFixedDim = "#83D2E0", onPrimaryFixed = "#001F24", onPrimaryFixedVariant = "#004F58",
    secondaryFixed = "#CDE7EC", secondaryFixedDim = "#B1CBD0", onSecondaryFixed = "#051F23", onSecondaryFixedVariant = "#334B4F",
    tertiaryFixed = "#DAE1FF", tertiaryFixedDim = "#BBC6EA", onTertiaryFixed = "#101A37", onTertiaryFixedVariant = "#3C4664",
)

private val oceanTheme: ColorsScheme = colorsScheme(
    lightColorScheme = oceanLightScheme,
    darkColorScheme = oceanDarkScheme,
)

object SetThemeEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SetTheme"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Overrides the whole app's Material 3 color scheme at runtime — light and dark " +
                    "— until ResetTheme is fired or the app restarts. Use it for server-controlled " +
                    "theming/branding, multi-tenant white-labeling, or accent themes chosen by the user. " +
                    "colorsScheme(lightColorScheme, darkColorScheme) requires a complete colorScheme(...) for " +
                    "each mode — all 46 roles of Compose Material3's ColorScheme, each as a hex String (e.g. " +
                    "\"#006874\"), with no default values."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Apply an \"ocean\" theme to the whole app") {
                Button(
                    text = "Apply theme with SetTheme",
                    events = {
                        SetTheme(
                            trigger = EventTriggers.onClick(),
                            colorsScheme = oceanTheme
                        )
                    }
                )
                ShowroomNote(
                    "The effect isn't local to this card — it changes the whole app's colors, on any " +
                        "screen, until you fire ResetTheme or close the app. Navigate to another rail tab " +
                        "after applying it to see the theme persist."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                SetTheme(
                    trigger = EventTriggers.onClick(),
                    colorsScheme = colorsScheme(
                        lightColorScheme = colorScheme(
                            primary = "#006874", onPrimary = "#FFFFFF",
                            primaryContainer = "#9EEFFD", onPrimaryContainer = "#001F24",
                            // ...the remaining 42 roles
                        ),
                        darkColorScheme = colorScheme(
                            primary = "#83D2E0", onPrimary = "#00363D",
                            primaryContainer = "#004F58", onPrimaryContainer = "#9EEFFD",
                            // ...the remaining 42 roles
                        )
                    )
                )
                """
            )

            ShowroomRelated(
                names = listOf("ResetTheme"),
                destination = "eventDetails"
            )
        }
    }
}
