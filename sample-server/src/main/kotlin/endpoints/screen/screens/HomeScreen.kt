package dev.catbit.mosaic.endpoints.screen.screens

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.server.builder.event.builders.networking.SendNetworkRequest
import dev.catbit.mosaic.server.builder.event.builders.screen.RefreshScreen
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import dev.catbit.mosaic.server.builder.tile.builders.containers.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.Text
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

suspend fun RoutingCall.respondHome() {
    respond(
        Screen(
            id = "home",
            tiles = {
                Column(
                    arrangement = arrangeToCenter(),
                    alignment = alignHorizontallyToCenter(),
                    tiles = {
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
                        Button(
                            text = "Make network call",
                            events = {
                                SendNetworkRequest(
                                    trigger = EventTriggers.onClick(),
                                    url = "https://naas.isalman.dev/no",
                                    method = HttpMethod.GET
                                )
                            },
                            style = {
                                margin(horizontal = 24)
                            }
                        )
                    },
                    style = {
                        windowInsets(windowInsetsSystemBars())
                        size(
                            width = fillHorizontally(),
                            height = fillVertically()
                        )
                        background(color("#babaca"))
                    }
                )
            }
        )
    )
}