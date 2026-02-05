package dev.catbit.mosaic

import dev.catbit.mosaic.endpoints.initial_graph.initialGraph
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        initialGraph()
    }
}
