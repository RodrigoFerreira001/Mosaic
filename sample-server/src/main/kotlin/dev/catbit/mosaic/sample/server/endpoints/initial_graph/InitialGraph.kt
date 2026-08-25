package dev.catbit.mosaic.sample.server.endpoints.initial_graph

import dev.catbit.mosaic.sample.server.endpoints.initial_graph.entries.EventDetails
import dev.catbit.mosaic.sample.server.endpoints.initial_graph.entries.Home
import dev.catbit.mosaic.sample.server.endpoints.initial_graph.entries.PaginationSample
import dev.catbit.mosaic.sample.server.endpoints.initial_graph.entries.TileDetails
import dev.catbit.mosaic.server.builder.animation.slideHorizontalReverseTransition
import dev.catbit.mosaic.server.builder.animation.slideHorizontalTransition
import dev.catbit.mosaic.server.builder.graph.Graph
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.initialGraph() {
    get("/initialGraph") {
        call.respond(
            Graph(
                startEntryId = "home",
                // Standard push/pop pair on the root navigator: new screens slide in from the
                // right, popping back mirrors it (previous screen slides back in from the left).
                defaultTransition = slideHorizontalTransition(),
                defaultPopTransition = slideHorizontalReverseTransition(),
                defaultPredictivePopTransition = slideHorizontalReverseTransition(),
            ) {
                Home()
                EventDetails()
                TileDetails()
                PaginationSample()
            }
        )
    }
}