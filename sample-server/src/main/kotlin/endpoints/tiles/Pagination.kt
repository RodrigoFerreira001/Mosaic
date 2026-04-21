package dev.catbit.mosaic.endpoints.tiles

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.server.builder.event.EventList
import dev.catbit.mosaic.server.builder.event.builders.event.UpdateEvents
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.RemoveTiles
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

fun Route.pagination() {
    get("pagination") {
        try {
            val page = call.request.queryParameters.getOrFail<Int>("page")

            delay(2.seconds)

            call.respond(
                if (page < 4) {
                    EventList {
                        RemoveTiles(
                            trigger = EventTriggers.inline(),
                            groupingTileId = "PAGINATED_LIST",
                            tileIds = listOf("LOADING")
                        )
                        AddTiles(
                            trigger = EventTriggers.inline(),
                            groupingTileId = "PAGINATED_LIST",
                        ) {
                            for (index in (page * 30)..(page * 30) + 30) {
                                Card(
                                    style = {
                                        margin(top = 8)
                                    }
                                ) {
                                    Row(
                                        style = {
                                            padding(
                                                horizontal = 16,
                                                vertical = 8
                                            )
                                        }
                                    ) {
                                        SimpleText(index.toString())
                                    }
                                }
                            }
                        }
                        UpdateEvents(
                            trigger = EventTriggers.inline(),
                            updates = {
                                update(
                                    eventId = "PAGINATION_EVENT",
                                    data = mapOf(
                                        "url" to "http://192.168.3.84:9090/pagination?page=${page + 1}"
                                    )
                                )
                            }
                        )
                    }
                } else EventList {
                    RemoveTiles(
                        trigger = EventTriggers.inline(),
                        groupingTileId = "PAGINATED_LIST",
                        tileIds = listOf("LOADING")
                    )
                }
            )

        } catch (e: Throwable) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Page not provided"
            )
        }
    }
}