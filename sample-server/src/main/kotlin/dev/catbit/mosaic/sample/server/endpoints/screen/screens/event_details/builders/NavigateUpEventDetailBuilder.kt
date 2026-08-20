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
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.event.builders.navigation.NavigateUp
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderQueryParameters
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.filledButton
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object NavigateUpEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "NavigateUp"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Pops the current destination off a navigator's stack — equivalent to the " +
                    "system back button, scoped to that navigator. Use it to close screens navigated to as a " +
                    "destination (full-screen dialogs, sub-flows) or to implement a custom back button in a " +
                    "top bar — this is exactly the pattern the \"eventDetails\" screen itself uses: the " +
                    "TopAppBar's back icon calls NavigateUp on the \"root\" navigator."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Navigate to a tile's page, then come back with NavigateUp") {
                ShowroomParagraph(
                    "First navigate to the Button tile's page; this very page's TopAppBar uses NavigateUp " +
                        "on its back button — the same event being documented here."
                )
                Button(
                    text = "View the Button tile",
                    buttonType = filledButton(),
                    events = {
                        TransformData(
                            trigger = EventTriggers.onClick(),
                            template = mapOf("event" to "Button"),
                            events = {
                                SetIncomingDataToNetworkParamsHolderQueryParameters(
                                    trigger = EventTriggers.onSuccess(),
                                    events = {
                                        Navigate(
                                            trigger = EventTriggers.onSuccess(),
                                            destination = "tileDetails",
                                            navigatorId = "root"
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                Button(
                    text = "Go back directly with NavigateUp",
                    buttonType = outlinedButton(),
                    events = {
                        NavigateUp(
                            trigger = EventTriggers.onClick(),
                            navigatorId = "root"
                        )
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                // Real usage in this app: EventDetailsScreenBuilder.kt, the TopAppBar's back button
                IconButton(
                    icon = icon("arrow_back"),
                    events = {
                        NavigateUp(
                            trigger = EventTriggers.onClick(),
                            navigatorId = "root"
                        )
                    }
                )
                """
            )

            ShowroomNote(
                "NavigateUp fails silently (onFailure) if the \"root\" navigator has no earlier entry left " +
                    "on its stack to go back to."
            )

            ShowroomRelated(
                names = listOf("Navigate", "NavigateClearingStack"),
                destination = "eventDetails"
            )
        }
    }
}
