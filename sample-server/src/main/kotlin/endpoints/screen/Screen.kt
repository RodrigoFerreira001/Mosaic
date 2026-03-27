package dev.catbit.mosaic.endpoints.screen

import dev.catbit.mosaic.endpoints.screen.screens.respondA
import dev.catbit.mosaic.endpoints.screen.screens.respondB
import dev.catbit.mosaic.endpoints.screen.screens.respondC
import dev.catbit.mosaic.endpoints.screen.screens.respondHome
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.screen() {
    get("screen/{screenId}") {
        when (val screenId = call.pathParameters["screenId"]) {
            "home" -> call.respondHome()
            "a" -> call.respondA()
            "b" -> call.respondB()
            "c" -> call.respondC()
            else -> call.respond(
                status = HttpStatusCode.NotFound,
                message = "Screen \"$screenId\" not found"
            )
        }
    }
}