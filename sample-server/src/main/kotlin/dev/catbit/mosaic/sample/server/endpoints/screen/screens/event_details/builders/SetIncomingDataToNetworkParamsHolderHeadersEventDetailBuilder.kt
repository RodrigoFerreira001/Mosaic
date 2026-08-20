package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailBuilder
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.networking.SendNetworkRequest
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderHeaders
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SetIncomingDataToNetworkParamsHolderHeadersEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SetIncomingDataToNetworkParamsHolderHeaders"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Stores the incomingData (must be Map<String, String>) as the headers of the " +
                    "chain's next network request. Typically used to inject authentication headers — a " +
                    "session cookie, a Bearer token — fetched via GetData for the following request. A cast " +
                    "failure (incomingData isn't a Map<String, String>) fires onFailure. Headers stored here " +
                    "are merged with SendNetworkRequest's schema headers; on a key collision, the schema " +
                    "wins: finalHeaders = holder + schema."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Send a custom header and see it echoed back by the API") {
                SimpleText(
                    id = "set_headers_status",
                    text = "Tap the button to fire the GET with the custom header."
                )
                Button(
                    text = "Send X-Mosaic-Demo: showroom to httpbin.org/headers",
                    events = {
                        TransformData(
                            trigger = EventTriggers.onClick(),
                            template = mapOf("X-Mosaic-Demo" to "showroom"),
                            events = {
                                SetIncomingDataToNetworkParamsHolderHeaders(
                                    trigger = EventTriggers.onSuccess(),
                                    events = {
                                        SendNetworkRequest(
                                            trigger = EventTriggers.onSuccess(),
                                            url = "https://httpbin.org/headers",
                                            method = HttpMethod.GET,
                                            events = {
                                                TransformData(
                                                    trigger = EventTriggers.onSuccess(),
                                                    template = mapOf("text" to "Header echoed: <|headers.X-Mosaic-Demo|>"),
                                                    events = {
                                                        UpdateTiles(
                                                            trigger = EventTriggers.onSuccess(),
                                                            updates = {
                                                                update("set_headers_status", incomingTileUpdateData())
                                                            }
                                                        )
                                                    }
                                                )
                                                UpdateTiles(
                                                    trigger = EventTriggers.onFailure(),
                                                    updates = {
                                                        update(
                                                            "set_headers_status",
                                                            inlineTileUpdateData("text" to "Request failed")
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                ShowroomNote(
                    "httpbin.org/headers returns exactly the headers it received, as JSON — a reliable way " +
                        "to prove the X-Mosaic-Demo header really left the holder and reached the request, " +
                        "without needing your own backend to inspect it."
                )
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                GetData(trigger = EventTriggers.onStart(), readings = {
                    reading(segmentedDataBase("auth"), singleAccessMode("sessionCookie"))
                }, events = {
                    TransformData(trigger = EventTriggers.onSuccess(), template = mapOf("Cookie" to "<||>"), events = {
                        SetIncomingDataToNetworkParamsHolderHeaders(trigger = EventTriggers.onSuccess(), events = {
                            SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/protected", method = HttpMethod.GET)
                        })
                    })
                })
                """
            )

            ShowroomRelated(
                names = listOf("SendNetworkRequest", "SetIncomingDataToNetworkParamsHolderBody", "GetData"),
                destination = "eventDetails"
            )
        }
    }
}
