package dev.catbit.mosaic.sample.server.endpoints.screen

import dev.catbit.mosaic.sample.server.endpoints.screen.screens.respondAbout
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.respondEvents
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.respondExtend
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.respondHome
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.respondMechanisms
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.respondStyle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.respondTiles
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.screen() {
    get("screens/{screenId}") {
        when (call.pathParameters["screenId"]) {
            "home" -> call.respondHome()
            "about" -> call.respondAbout()
            "style" -> call.respondStyle()
            "tiles" -> call.respondTiles()
            "events" -> call.respondEvents()
            "extend" -> call.respondExtend()
            "mechanisms" -> call.respondMechanisms()
        }
    }
}