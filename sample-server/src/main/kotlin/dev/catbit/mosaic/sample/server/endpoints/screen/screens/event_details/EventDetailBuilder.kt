package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details

import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

/**
 * One implementation per event name, rendering the detail content shown below the
 * "eventDetails" app bar (see [dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details.EventDetailsScreenBuilder]).
 * [EventDetailBuilderManager] picks the first builder whose [canBuild] matches.
 */
interface EventDetailBuilder {
    fun canBuild(eventName: String): Boolean
    fun TileSchemaBuilderScope.buildDetail(eventName: String)
}
