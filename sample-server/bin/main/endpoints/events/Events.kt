package dev.catbit.mosaic.endpoints.events

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.server.builder.event.EventList
import dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet.DisplayBottomSheet
import dev.catbit.mosaic.server.builder.icon
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

fun Route.events() {
    post("events/{eventId}") {
        val eventId = call.parameters["eventId"] ?: return@post
        when (eventId) {
            "helloName" -> {

                delay(1.seconds)

                val body = call.receive<JsonObject>()
                val name = body["name"]?.jsonPrimitive?.contentOrNull ?: "?"
                call.respond(
                    EventList {
                        DisplayBottomSheet(
                            trigger = EventTriggers.inline()
                        ) {
                            Column(
                                style = {
                                    size(height = fixedVertically(200))
                                    padding(horizontal = 24)
                                },
                                arrangement = arrangeToCenter(),
                                alignment = alignHorizontallyToCenter()
                            ) {
                                SimpleText(text = "Olá $name!")
                            }
                        }
                    }
                )
            }
        }
    }

    get("events/{eventId}") {
        delay(1.seconds)
        call.respond(
            EventList {
                DisplayBottomSheet(
                    trigger = EventTriggers.inline()
                ) {
                    Column(
                        style = {
                            size(
                                height = fixedVertically(200)
                            )
                            padding(horizontal = 24)
                        },
                        arrangement = arrangeToCenter(),
                        alignment = alignHorizontallyToCenter()
                    ) {
                        Icon(icon = icon("waving_hand"))
                        SimpleText(text = "Hello there!")
                    }
                }
            }
        )
    }
}