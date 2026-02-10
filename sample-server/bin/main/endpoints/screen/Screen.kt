package dev.catbit.mosaic.endpoints.screen

import dev.catbit.mosaic.endpoints.screen.screens.respondHome
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.screen() {
    get("screen/{screenId}") {
        when (val screenId = call.pathParameters["screenId"]) {
            "home" -> call.respondHome()
            else -> call.respond(
                status = HttpStatusCode.NotFound,
                message = "Screen \"$screenId\" not found"
            )
        }
    }
}