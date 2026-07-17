package dev.catbit.mosaic.sample.server.endpoints.version

import dev.catbit.mosaic.core.data.responses.version.VersionResponse
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.version() {
    get("/version") {
        call.respond(VersionResponse(version = 1))
    }
}
