package dev.catbit.mosaic

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.screen.ScreenBuilder
import dev.catbit.mosaic.server.builder.tile.builders.buttons.Button
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {

            ScreenBuilder(
                id = "home",
                tiles = {
                    Button(
                        events = {
                            AddTiles(
                                trigger = EventTriggers.onClick(),
                                groupingTileId = "Teste",
                                position = dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema.InsertionPosition.Start,
                                tiles = {

                                },
                                events = {

                                },

                            )
                        },
                        style = {
                            size(
                                width = fillHorizontally()
                            )
                            background(
                                color = color("#babaca")
                            )
                        },
                        text = "Olá"
                    )
                }
            )

            call.respondText("Hello World!")
        }
    }
}
