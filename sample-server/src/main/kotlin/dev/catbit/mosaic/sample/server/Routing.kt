package dev.catbit.mosaic.sample.server

import dev.catbit.mosaic.sample.server.endpoints.initial_graph.initialGraph
import dev.catbit.mosaic.sample.server.endpoints.screen.screen
import dev.catbit.mosaic.sample.server.endpoints.version.version
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        initialGraph()
        screen()
        version()
    }
}
