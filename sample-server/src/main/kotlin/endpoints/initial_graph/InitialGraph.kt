package dev.catbit.mosaic.endpoints.initial_graph

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState
import dev.catbit.mosaic.server.builder.event.builders.screen.GetScreen
import dev.catbit.mosaic.server.builder.event.builders.screen.successState
import dev.catbit.mosaic.server.builder.graph.Entry
import dev.catbit.mosaic.server.builder.graph.Graph
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.initialGraph() {
    get("/initialGraph") {
        call.respond(
            Graph(
                entries = {
                    Entry(
                        screenId = "home",
                        initialTiles = {
                            Column(
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter(),
                                tiles = {
                                    SimpleText(
                                        text = "Carregando",
                                        style = {
                                            size(
                                                width = wrapHorizontally()
                                            )
                                        }
                                    )
                                }
                            )
                        },
                        initialEvents = {
                            GetScreen(
                                trigger = EventTriggers.onDisplay(),
                                events = {
                                    ChangeScreenState(
                                        trigger = EventTriggers.onSuccess(),
                                        state = successState()
                                    )
                                }
                            )
                        },
                        failureTiles = {

                        },
                        failureEvents = {

                        }
                    )
                },
                startEntryId = "home"
            )
        )
    }
}