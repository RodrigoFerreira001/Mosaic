package dev.catbit.mosaic.sample.server.endpoints.initial_graph

import dev.catbit.mosaic.sample.server.endpoints.initial_graph.entries.Home
import dev.catbit.mosaic.server.builder.graph.Graph
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.initialGraph() {
    get("/initialGraph") {
        call.respond(
            Graph(
                startEntryId = "home"
            ) {
                Home()
            }
        )
    }
}