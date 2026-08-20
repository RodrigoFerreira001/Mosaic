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
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SendNetworkRequestEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SendNetworkRequest"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Makes an HTTP request to a URL and propagates the response through its " +
                    "child events — the foundation of any API call. Use it for any server API call: CRUD, " +
                    "authentication, data fetching. The response body is delivered as incomingData to the " +
                    "child events — JSON becomes a Map/List/primitive value, any other content type becomes " +
                    "a ByteArray. The request body is resolved as body (schema) ?? holder.body (set by " +
                    "SetIncomingDataToNetworkParamsHolderBody) — the schema always takes priority."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Real GET request to a public, unauthenticated API") {
                SimpleText(
                    id = "network_request_status_text",
                    text = "Tap the button to fire the request."
                )
                Button(
                    text = "Fetch https://jsonplaceholder.typicode.com/todos/1",
                    events = {
                        SendNetworkRequest(
                            trigger = EventTriggers.onClick(),
                            url = "https://jsonplaceholder.typicode.com/todos/1",
                            method = HttpMethod.GET,
                            events = {
                                UpdateTiles(
                                    trigger = EventTriggers.onStart(),
                                    updates = {
                                        update(
                                            tileId = "network_request_status_text",
                                            updateData = inlineTileUpdateData("text" to "Sending request...")
                                        )
                                    }
                                )
                                TransformData(
                                    trigger = EventTriggers.onSuccess(),
                                    template = mapOf("text" to "onSuccess · title received: \"<|title|>\""),
                                    events = {
                                        UpdateTiles(
                                            trigger = EventTriggers.onSuccess(),
                                            updates = { update("network_request_status_text", incomingTileUpdateData()) }
                                        )
                                    }
                                )
                                UpdateTiles(
                                    trigger = EventTriggers.onFailure(),
                                    updates = {
                                        update(
                                            tileId = "network_request_status_text",
                                            updateData = inlineTileUpdateData("text" to "onFailure · connection failure or non-2xx response")
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
                SendNetworkRequest(
                    trigger = EventTriggers.onClick(),
                    url = "https://jsonplaceholder.typicode.com/todos/1",
                    method = HttpMethod.GET,
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onSuccess(),
                            updates = { update("result_text", incomingTileUpdateData()) }
                        )
                        Navigate(
                            trigger = EventTriggers.onNetworkFailure(401),
                            destination = "login",
                            navigatorId = "root"
                        )
                    }
                )
                """
            )
            ShowroomNote(
                "onNetworkResponse(code)/onNetworkFailure(code) replace onSuccess/onFailure for that " +
                    "specific HTTP status when declared as a child. onFailure never fires for codes with a " +
                    "matching onNetworkFailure(code)."
            )

            ShowroomRelated(
                names = listOf("SetIncomingDataToNetworkParamsHolderBody", "SetIncomingDataToNetworkParamsHolderQueryParameters", "DownloadFile"),
                destination = "eventDetails"
            )
        }
    }
}
