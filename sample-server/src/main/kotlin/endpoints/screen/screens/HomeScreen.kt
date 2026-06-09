package dev.catbit.mosaic.endpoints.screen.screens

import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

suspend fun RoutingCall.respondHome() {
    respond(
        Screen(id = "home") {
            Column(
                style = {
                    windowInsets(windowInsetsSystemBars())
                    size(
                        width = fillHorizontally(),
                        height = fillVertically()
                    )
                },
                arrangement = arrangeToCenter(),
                alignment = alignHorizontallyToCenter()
            ) {
                SimpleText(text = "Home")
            }
        }
    )
}
