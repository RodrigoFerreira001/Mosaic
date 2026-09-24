package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.theme.SetColorMode
import dev.catbit.mosaic.server.builder.event.builders.theme.colorModeDark
import dev.catbit.mosaic.server.builder.event.builders.theme.colorModeLight
import dev.catbit.mosaic.server.builder.event.builders.theme.colorModeSystemDefault
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row

object SetColorModeEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SetColorMode"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Sets the app's color mode — light, dark, or following the system setting — " +
                    "overriding whatever was set before. Unlike SetTheme, which swaps the Material color roles " +
                    "themselves, this only decides which of the already-installed light/dark schemes is active. " +
                    "Use it for a user-facing \"appearance\" setting."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Switch the app's color mode") {
                Row(
                    style = { size(width = fillHorizontally(), height = wrapVertically()) },
                    arrangement = arrangeHorizontallySpacedBy(8)
                ) {
                    Button(
                        text = "Light",
                        buttonType = outlinedButton(),
                        events = {
                            SetColorMode(
                                trigger = EventTriggers.onClick(),
                                colorMode = colorModeLight()
                            )
                        }
                    )
                    Button(
                        text = "Dark",
                        buttonType = outlinedButton(),
                        events = {
                            SetColorMode(
                                trigger = EventTriggers.onClick(),
                                colorMode = colorModeDark()
                            )
                        }
                    )
                    Button(
                        text = "System",
                        buttonType = outlinedButton(),
                        events = {
                            SetColorMode(
                                trigger = EventTriggers.onClick(),
                                colorMode = colorModeSystemDefault()
                            )
                        }
                    )
                }
                ShowroomNote(
                    "The effect isn't local to this card — it changes the whole app's color mode, on any " +
                        "screen, until another SetColorMode is fired or the app restarts. Navigate to another " +
                        "rail tab after applying it to see the mode persist."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                SetColorMode(
                    trigger = EventTriggers.onClick(),
                    colorMode = colorModeDark()
                )
                """
            )

            ShowroomRelated(
                names = listOf("SetTheme", "ResetTheme"),
                destination = "eventDetails"
            )
        }
    }
}
