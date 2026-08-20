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
import dev.catbit.mosaic.server.builder.data.singleAccessMode
import dev.catbit.mosaic.server.builder.data.tile
import dev.catbit.mosaic.server.builder.event.builders.data.GetData
import dev.catbit.mosaic.server.builder.event.builders.data.TransformData
import dev.catbit.mosaic.server.builder.event.builders.networking.SendNetworkRequest
import dev.catbit.mosaic.server.builder.event.builders.networking.SetIncomingDataToNetworkParamsHolderBody
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.incomingTileUpdateData
import dev.catbit.mosaic.server.builder.event.builders.tiles.inlineTileUpdateData
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText

object SetIncomingDataToNetworkParamsHolderBodyEventDetailBuilder : EventDetailBuilder {

    override fun canBuild(eventName: String) = eventName == "SetIncomingDataToNetworkParamsHolderBody"

    override fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "Stores the incomingData as the body of the chain's next network request, in " +
                    "the NetworkParametersHolder. Use it when you need to pass the output of a previous " +
                    "event (form data read with GetData, for example) as the body of the following " +
                    "SendNetworkRequest, without rebuilding the map manually. A null incomingData fires " +
                    "onFailure and nothing is stored; SendNetworkRequest's schema body, if present, always " +
                    "wins over the value stored here."
            )

            ShowroomSectionTitle("Interactive demo")
            ShowroomDemoCard(title = "Type a title and send it as the body of a real POST") {
                TextField(
                    id = "set_body_title_input",
                    placeholder = "Post title",
                    style = { size(width = fillHorizontally(), height = wrapVertically()) }
                )
                SimpleText(
                    id = "set_body_status",
                    text = "Tap the button to fire the POST."
                )
                Button(
                    text = "Send as body to jsonplaceholder.typicode.com/posts",
                    events = {
                        GetData(
                            trigger = EventTriggers.onClick(),
                            readings = {
                                reading(
                                    dataSource = tile("set_body_title_input", "text"),
                                    accessMode = singleAccessMode("title")
                                )
                            },
                            events = {
                                SetIncomingDataToNetworkParamsHolderBody(
                                    trigger = EventTriggers.onSuccess(),
                                    events = {
                                        SendNetworkRequest(
                                            trigger = EventTriggers.onSuccess(),
                                            url = "https://jsonplaceholder.typicode.com/posts",
                                            method = HttpMethod.POST,
                                            events = {
                                                TransformData(
                                                    trigger = EventTriggers.onSuccess(),
                                                    template = mapOf("text" to "Created with id <|id|> — body sent: <|title|>"),
                                                    events = {
                                                        UpdateTiles(
                                                            trigger = EventTriggers.onSuccess(),
                                                            updates = {
                                                                update("set_body_status", incomingTileUpdateData())
                                                            }
                                                        )
                                                    }
                                                )
                                                UpdateTiles(
                                                    trigger = EventTriggers.onFailure(),
                                                    updates = {
                                                        update(
                                                            "set_body_status",
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
            }

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                GetData(trigger = EventTriggers.onClick(), readings = { reading(screenSegmentedData("form"), fullAccessMode()) }, events = {
                    SetIncomingDataToNetworkParamsHolderBody(trigger = EventTriggers.onSuccess(), events = {
                        SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/create", method = HttpMethod.POST)
                    })
                })
                """
            )
            ShowroomNote(
                "GetData reads the TextField's text as {\"title\": \"...\"}; " +
                    "SetIncomingDataToNetworkParamsHolderBody stores that map as the body on the holder; " +
                    "and the following SendNetworkRequest, without providing its own body, uses the " +
                    "stored value. The jsonplaceholder.typicode.com API is fake (it doesn't persist " +
                    "anything for real), but it always echoes the body it received back with a new id " +
                    "— which is why the final text shows both the id and the title you typed."
            )

            ShowroomRelated(
                names = listOf("SendNetworkRequest", "SetIncomingDataToNetworkParamsHolderHeaders", "GetData"),
                destination = "eventDetails"
            )
        }
    }
}
