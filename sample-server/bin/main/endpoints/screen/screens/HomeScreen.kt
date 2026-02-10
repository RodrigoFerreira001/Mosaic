package dev.catbit.mosaic.endpoints.screen.screens

import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

suspend fun RoutingCall.respondHome() {
    respond(
        Screen(
            id = "home",
            tiles = {
                Button(
                    text = "Home",
                    events = {

                    }
                )
            }
        )
    )
}