package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.screen.RefreshScreen
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton

object RefreshScreenEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "RefreshScreen"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Reloads the current screen from scratch — resets it to Initial, fetches the " +
                    "definition from the server, and applies the result automatically, with no manual steps. " +
                    "Use it for the simple pull-to-refresh or \"try again\" pattern, when the goal is to " +
                    "re-fetch and re-render the whole screen without manually managing state transitions: " +
                    "GetScreen and ChangeScreenState are already baked in, unlike GetScreen alone, which only " +
                    "delivers the data without applying it."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Reload this very page with RefreshScreen") {
                ShowroomParagraph(
                    "Clicking the button below fires a real RefreshScreen: the whole screen goes back to " +
                        "Initial (a brief loading flash), fetches the definition from the server again, and " +
                        "re-renders it — exactly like a pull-to-refresh would, just triggered by a click " +
                        "instead of a drag."
                )
                Button(
                    text = "Reload with RefreshScreen",
                    buttonType = filledButton(),
                    events = {
                        RefreshScreen(
                            trigger = EventTriggers.onClick(),
                            events = {
                                DisplaySnackbar(
                                    trigger = EventTriggers.onFailure(),
                                    message = "Failed to reload the screen"
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "There's no success snackbar here on purpose: when RefreshScreen applies the new " +
                        "definition, the entire screen is replaced — including this very demo card — so the " +
                        "visible \"result\" is the whole page reloading before your eyes."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                RefreshScreen(
                    trigger = EventTriggers.onPull(),
                    events = {
                        StopRefreshing(trigger = EventTriggers.onSuccess(), tileId = "ptr_container")
                        StopRefreshing(trigger = EventTriggers.onFailure(), tileId = "ptr_container")
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("GetScreen", "ChangeScreenState", "StopRefreshing"),
                destination = "eventDetails"
            )
        }
    }
}
