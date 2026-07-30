package dev.catbit.mosaic.sample.server.endpoints.screen.screens.event_details

import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

class EventDetailBuilderManager(
    private val builders: List<EventDetailBuilder>
) {
    fun TileSchemaBuilderScope.buildDetail(eventName: String) {
        val builder = builders.first { it.canBuild(eventName) }
        with(builder) { buildDetail(eventName) }
    }
}
