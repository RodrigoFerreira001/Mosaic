package dev.catbit.mosaic.endpoints.tiles

import dev.catbit.mosaic.server.builder.icon
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToCenter
import dev.catbit.mosaic.server.builder.placement.arrangeToCenter
import dev.catbit.mosaic.server.builder.tile.TileList
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.image.Icon
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

fun Route.tiles() {
    get("tiles/{tileId}") {
        delay(3.seconds)
        call.respond(
            TileList {
                Column(
                    style = {
                        size(
                            height = fixedVertically(200)
                        )
                        margin(horizontal = 24)
                    },
                    arrangement = arrangeToCenter(),
                    alignment = alignHorizontallyToCenter()
                ) {
                    Icon(icon = icon("check_circle"))
                    SimpleText(text = "Loaded successfully")
                }
            }
        )
    }
}