package dev.catbit.mosaic.endpoints.screen

import dev.catbit.mosaic.endpoints.screen.screens.respondHome
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.screen() {
    get("screens/{screenId}") {
        when (val screenId = call.pathParameters["screenId"]) {
            "home" -> call.respondHome()
        }
    }
}