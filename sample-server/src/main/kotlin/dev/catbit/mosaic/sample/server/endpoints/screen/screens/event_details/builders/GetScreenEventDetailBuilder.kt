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
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState
import dev.catbit.mosaic.server.builder.event.builders.screen.GetScreen
import dev.catbit.mosaic.server.builder.event.builders.screen.successState
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledTonalButton
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium

object GetScreenEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "GetScreen"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Fetches a screen's definition from the server and exposes the result as " +
                    "incomingData — without automatically applying it to the current screen. Use it when you " +
                    "need to inspect the fetched ScreenModel before deciding how to apply it, or when you want " +
                    "to control the state transition manually (for example, showing a custom loading state " +
                    "while fetching). For the simple \"reload this screen\" case, prefer RefreshScreen, which " +
                    "already wraps GetScreen + ChangeScreenState. This is how every entry in this app " +
                    "(HomeScreenBuilder, each rail tab) loads its initial content: GetScreen on onDisplay, " +
                    "followed by ChangeScreenState(successState()) on onSuccess."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Fetch this very screen's definition without navigating away") {
                SimpleText(
                    id = "get_screen_status",
                    text = "Not fetched yet",
                    typography = typographyBodyMedium()
                )
                Button(
                    text = "Fetch with GetScreen (no navigation)",
                    buttonType = filledTonalButton(),
                    events = {
                        GetScreen(
                            trigger = EventTriggers.onClick(),
                            events = {
                                DisplaySnackbar(
                                    trigger = EventTriggers.onSuccess(),
                                    message = "ScreenModel received as incomingData — not applied, just demonstrated"
                                )
                                DisplaySnackbar(
                                    trigger = EventTriggers.onFailure(),
                                    message = "Failed to fetch the screen"
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "Notice this GetScreen has NO ChangeScreenState(successState()) chained after it — that's " +
                        "why the screen doesn't flash/reload on click. That's exactly the practical difference " +
                        "from RefreshScreen: GetScreen alone only fetches and hands over the data; applying it " +
                        "is up to you."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                // Pattern used by every entry in this app (see Graph.entry's default initialEvents)
                GetScreen(
                    trigger = EventTriggers.onDisplay(),
                    events = {
                        ChangeScreenState(trigger = EventTriggers.onSuccess(), state = successState())
                        ChangeScreenState(trigger = EventTriggers.onFailure(), state = failureState())
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("RefreshScreen", "ChangeScreenState", "Navigate"),
                destination = "eventDetails"
            )
        }
    }
}
