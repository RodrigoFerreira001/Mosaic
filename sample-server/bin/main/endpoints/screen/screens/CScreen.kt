package dev.catbit.mosaic.endpoints.screen.screens

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.event.builders.screen.RefreshScreen
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

suspend fun RoutingCall.respondC() {
    respond(
        Screen(
            id = "c",
        ) {
            Column(
                style = {
                    size(height = fillVertically())
                    background(color = color("#0000FF"))
                },
                arrangement = arrangeToCenter(),
                alignment = alignHorizontallyToCenter()
            ) {
                SimpleText(text = "Tela C")
                Button(
                    text = "Reload",
                    events = {
                        RefreshScreen(
                            trigger = EventTriggers.onClick()
                        )
                    },
                    style = {
                        margin(horizontal = 24)
                    }
                )
            }
        }
    )
}