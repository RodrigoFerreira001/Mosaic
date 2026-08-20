package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.system.CheckIfHasInternetConnection
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object CheckIfHasInternetConnectionEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "CheckIfHasInternetConnection"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Checks whether the device currently has an active internet connection at the " +
                    "moment the event runs. Use it before attempting network operations in offline-first " +
                    "scenarios, avoiding firing a SendNetworkRequest that's bound to time out when it's already " +
                    "known there's no connectivity."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Button checks the connection for real") {
                SimpleText(
                    id = "check_internet_status",
                    text = "Status: waiting"
                )
                Button(
                    text = "Check connection",
                    events = {
                        CheckIfHasInternetConnection(
                            trigger = EventTriggers.onClick(),
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onSuccess(),
                                    updates = {
                                        update(
                                            tileId = "check_internet_status",
                                            updateData = inlineTileUpdateData("text" to "Status: connected ✅")
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update(
                                            tileId = "check_internet_status",
                                            updateData = inlineTileUpdateData("text" to "Status: no connection ❌")
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
                CheckIfHasInternetConnection(
                    trigger = EventTriggers.onDisplay(),
                    events = {
                        SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/data", method = HttpMethod.GET)
                        DisplaySnackbar(trigger = EventTriggers.onFailure(), message = "No internet connection")
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("BroadcastToSystem", "SendNetworkRequest", "OpenExternalLink"),
                destination = "eventDetails"
            )
        }
    }
}
