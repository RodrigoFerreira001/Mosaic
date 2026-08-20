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
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.navigation.Navigate
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderQueryParameters
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.buttons.outlinedButton

object SetIncomingDataToNetworkParamsHolderQueryParametersEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SetIncomingDataToNetworkParamsHolderQueryParameters"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Stores the incomingData as the query parameters of the chain's next network " +
                    "request — including the automatic GetScreen fired by a navigation. This is literally " +
                    "the mechanism that makes EVERY card-to-card navigation work in this sample app: every " +
                    "card in the Tiles/Events catalog (CatalogItem) and every \"Related\" chip at the bottom " +
                    "of a detail page uses exactly this sequence — " +
                    "TransformData(template = mapOf(\"event\" to name)) → " +
                    "SetIncomingDataToNetworkParamsHolderQueryParameters → Navigate. The destination entry " +
                    "(\"eventDetails\"/\"tileDetails\") automatically fires a GetScreen when displayed, and " +
                    "that GetScreen uses the query parameters stored here — that's how the screen knows " +
                    "which event/tile to show, by reading request.queryParameters[\"event\"] on the server."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "You already used this event to get here") {
                ShowroomNote(
                    "Seriously — the \"$eventName\" button you tapped on the Events screen, or the " +
                        "\"Related\" chip that brought you here from another page, fired exactly this chain. " +
                        "The button below fires it again, this time to a neighboring page."
                )
                Button(
                    text = "View the Button tile",
                    buttonType = outlinedButton(),
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
                                            navigatorId = "root",
                                            destination = "tileDetails"
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                // Real usage in this app: CatalogItem.kt and ShowroomComponents.kt (ShowroomRelatedChip)
                TransformData(
                    trigger = EventTriggers.onClick(),
                    template = mapOf("event" to name),
                    events = {
                        SetIncomingDataToNetworkParamsHolderQueryParameters(
                            trigger = EventTriggers.onSuccess(),
                            events = {
                                Navigate(
                                    trigger = EventTriggers.onSuccess(),
                                    navigatorId = "root",
                                    destination = destination
                                )
                            }
                        )
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("Navigate", "TransformData", "SetIncomingDataToNetworkParamsHolderBody"),
                destination = "eventDetails"
            )
        }
    }
}
