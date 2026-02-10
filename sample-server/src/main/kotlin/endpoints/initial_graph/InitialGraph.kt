package dev.catbit.mosaic.endpoints.initial_graph

import dev.catbit.mosaic.server.builder.graph.Entry
import dev.catbit.mosaic.server.builder.graph.Graph
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
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
                            Button(
                                text = "Loading home"
                            )
                        },
                        initialEvents = {

                        },
                        failureTiles =  {

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