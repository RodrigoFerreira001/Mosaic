package dev.catbit.mosaic.sample.server.endpoints.screen

import dev.catbit.mosaic.sample.server.endpoints.screen.screens.about.AboutScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailsScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.events.EventsScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.extend.ExtendScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.get_started.GetStartedScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.home.HomeScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.mechanisms.MechanismsScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.pagination_sample.PaginationSampleScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.style.StyleScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailsScreenBuilder
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tiles.TilesScreenBuilder
import dev.catbit.mosaic.sample.server.exceptions.UnknownScreenException
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

private val screenBuilderManager = ScreenBuilderManager(
    builders = listOf(
        HomeScreenBuilder,
        AboutScreenBuilder,
        StyleScreenBuilder,
        TilesScreenBuilder,
        EventsScreenBuilder,
        EventDetailsScreenBuilder,
        TileDetailsScreenBuilder,
        GetStartedScreenBuilder,
        ExtendScreenBuilder,
        MechanismsScreenBuilder,
        PaginationSampleScreenBuilder,
    )
)

fun Route.screen() {
    get("screens/{screenId}") {
        val screenId = call.pathParameters["screenId"] ?: return@get

        try {
            call.respond(
                with(screenBuilderManager) {
                    call.build(screenId)
                }
            )
        } catch (_: UnknownScreenException) {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
